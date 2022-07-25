package com.dnd.mountclim.config.jwt;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dnd.mountclim.config.auth.PrincipalDetails;

@Component
public class JwtTokenProvider implements InitializingBean {

	@Value("${jwt.secret}")
    private String jwtSecret;
	
	@Value("${jwt.expiration.time}")
    private int jwtExpirationTime;
	
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
	
	public String validateToken(String token) {
		try {			
			DecodedJWT decodedJWT = JWT.decode(token);
			if(decodedJWT.getExpiresAt().before(new Date())) {
				throw new JWTVerificationException("The Token has expired on " + decodedJWT.getExpiresAt());
			}
		} catch(Exception e) {
			throw e;
		}
		return JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token)
				.getClaim("username").asString();
	}
}
