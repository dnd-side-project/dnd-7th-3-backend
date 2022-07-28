package com.dnd.mountclim.config.jwt;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dnd.mountclim.config.auth.PrincipalDetails;

@Component
public class JwtTokenProvider implements InitializingBean {

	@Value("${jwt.secret}")
    private String jwtSecret;
	
	@Value("${jwt.expiration.time}")
    private int jwtExpirationTime;
	
	@Value("${jwt.refresh.expiration.time}")
    private int jwtRefreshExpirationTime;
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	public String accessCreateToken(PrincipalDetails principalDetailis) {
		return JWT.create()
				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationTime))
				.withClaim("id", principalDetailis.getMemberEntity().getId())
				.withClaim("username", principalDetailis.getMemberEntity().getUsername())
				.sign(Algorithm.HMAC512(jwtSecret));
	}
	
	public String refreshCreateToken(PrincipalDetails principalDetailis) {
		String refreshToken = JWT.create()
				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationTime))
				.withClaim("id", principalDetailis.getMemberEntity().getId())
				.withClaim("username", principalDetailis.getMemberEntity().getUsername())
				.sign(Algorithm.HMAC512(jwtSecret));
		
		redisTemplate.opsForValue().set(principalDetailis.getMemberEntity().getUsername(), refreshToken, Duration.ofSeconds(jwtRefreshExpirationTime));
		
		return refreshToken;
	}
	
	public String getRefreshToken(String username) {
		String redisRefreshToken = redisTemplate.opsForValue().get(username); 	
		return redisRefreshToken;
	}
	
	public String getUsernameFromToken(String token) {
		DecodedJWT decodedJWT = JWT.decode(token);
		String username = decodedJWT.getSubject();
		return username;
	}
	
	public boolean expireToken(String token) {
		DecodedJWT decodedJWT = JWT.decode(token);
		if(decodedJWT.getExpiresAt().before(new Date())) {
			return true;
		}
		return false;
	}
	
	public String validateToken(String token) {
		return JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token)
				.getClaim("username").asString();
	}
}
