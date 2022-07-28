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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private JwtTokenProvider jwtTokenProvider;
	private MemberRepository memberRepository;

	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, MemberRepository memberRepository) {
		super(authenticationManager);
		this.memberRepository = memberRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("JwtAuthorizationFilter doFilterInternal : 진입");
		try {
			String header = request.getHeader("Authorization");
			if(header == null || !header.startsWith("Bearer ")) {
				chain.doFilter(request, response);
				return;
			}
			logger.info("header : " + header);
			String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
			
			if(jwtTokenProvider.expireToken(accessToken)) {			
				String username = jwtTokenProvider.getUsernameFromToken(accessToken);					// accessToken의 username 가져오기
				MemberEntity memberEntity = memberRepository.findByUsername(username);					
				if(memberEntity != null) {																// DB에 사용자 정보가 있는 경우				
					PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);	
					String refreshToken = jwtTokenProvider.getRefreshToken(username);
					if(refreshToken != null) {															// refreshToken이 있는 경우
						String newAccessToken = jwtTokenProvider.accessCreateToken(principalDetails);
						response.addHeader("Authorization", "Bearer " + newAccessToken);
						Authentication authentication =
								new UsernamePasswordAuthenticationToken(
										principalDetails, 
										null,
										principalDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} else {
						throw new Exception("토큰 정보가 만료되었습니다. 다시 로그인해주세요.");
					}
				} else {
					throw new Exception("DB에 사용자 정보가 없습니다.");
				}
			} else {
				String username = jwtTokenProvider.validateToken(accessToken);
				MemberEntity memberEntity = memberRepository.findByUsername(username);
				PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);																		
				
				Authentication authentication =
						new UsernamePasswordAuthenticationToken(
								principalDetails, 
								null,
								principalDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}	
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
