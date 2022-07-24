package com.dnd.mountclim.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dnd.mountclim.common.dto.LoginRequestDto;
import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration.time}")
    private int JWT_EXPIRATION_TIME;
	
	private final AuthenticationManager authenticationManager;
	private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	// /login 요청시에 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		logger.info("Authentication attemptAuthentication");
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(), 
						loginRequestDto.getPassword());

		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
		logger.info("principalDetailis : " + principalDetailis.getMemberEntity().getUsername());
		return authentication;
	}

	// JWT Token 생성해서 response에 담아주기
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.info("Authentication successfulAuthenticatio");
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

		String jwtToken = JWT.create()
				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
				.withClaim("id", principalDetailis.getMemberEntity().getId())
				.withClaim("username", principalDetailis.getMemberEntity().getUsername())
				.sign(Algorithm.HMAC512(JWT_SECRET));

		logger.info("jwtToken : " + jwtToken);
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
}
