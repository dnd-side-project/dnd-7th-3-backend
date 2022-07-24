package com.dnd.mountclim.config.jwt;

import java.io.IOException;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Value("${jwt.secret}")
    private String JWT_SECRET;
	
	private MemberRepository memberRepository;
	private static Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
		super(authenticationManager);
		this.memberRepository = memberRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("doFilterInternal");
		String header = request.getHeader("Authorization");
		if(header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		String token = request.getHeader("Authorization")
				.replace("Bearer ", "");
		String username = JWT.require(Algorithm.HMAC512(JWT_SECRET)).build().verify(token)
				.getClaim("username").asString();
		if(username != null) {
			MemberEntity memberEntity = memberRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
			Authentication authentication =
					new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}
