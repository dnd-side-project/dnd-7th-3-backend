package com.dnd.mountclim.config.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.config.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PrincipalOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final JwtTokenProvider jwtTokenProvider;
	private static Logger logger = LoggerFactory.getLogger(PrincipalOAuth2SuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("PrincipalOAuth2SuccessHandler onAuthenticationSuccess : 진입");
		
		PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
		
		String accessToken = jwtTokenProvider.accessCreateToken(principalDetailis);
		String refreshToken = jwtTokenProvider.refreshCreateToken(principalDetailis);
		
		logger.info("[ OAuth ACCESSTOKEN ] : " + accessToken);
		logger.info("[ OAuth REFRESHTOKEN ] : " + refreshToken);
		
		/*
		 * 프론트엔드 ouath 로그인 요청 시 redirect url 지정
		 */
		String targetUrl = UriComponentsBuilder.fromUriString("/home")
				.queryParam("access_token", accessToken)
				.build().toUriString();
		
		response.addHeader("Authorization", "Bearer " + accessToken);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
