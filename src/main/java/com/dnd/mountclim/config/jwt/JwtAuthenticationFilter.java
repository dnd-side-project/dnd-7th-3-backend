package com.dnd.mountclim.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dnd.mountclim.common.dto.LoginRequestDto;
import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;	
	private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		logger.info("JwtAuthenticationFilter attemptAuthentication : 진입");
		
		ObjectMapper om = new ObjectMapper();
		LoginRequestDto loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("JwtAuthenticationFilter : " + loginRequestDto);

		// 유저네임패스워드 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginRequestDto.getUsername(), 
						loginRequestDto.getPassword());

		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);

		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
		logger.info("Authentication : " + principalDetailis.getMemberEntity().getUsername());
		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.info("JwtAuthenticationFilter successfulAuthentication : 진입");	
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
		String accessToken = jwtTokenProvider.accessCreateToken(principalDetailis);
		logger.info("JwtAuthenticationFilter 토큰생성완료 :" + accessToken);
		response.addHeader("Authorization", "Bearer " + accessToken);
	}
}
