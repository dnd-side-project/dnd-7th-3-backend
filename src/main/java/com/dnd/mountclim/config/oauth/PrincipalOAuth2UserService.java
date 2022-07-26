package com.dnd.mountclim.config.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.config.oauth.provider.GoogleUserInfo;
import com.dnd.mountclim.config.oauth.provider.OAuth2UserInfo;
import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private MemberRepository memberRepository;
	
	private static Logger logger = LoggerFactory.getLogger(PrincipalOAuth2UserService.class);
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		logger.info("PrincipalOAuth2UserService loadUser : 진입");
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			logger.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		MemberEntity memberEntity = memberRepository.findByUsername(username);
		if(memberEntity == null) {
			memberEntity = MemberEntity.builder()
									.username(username)
									.password(password)
									.email(email)
									.role(role)
									.provider(provider)
									.providerId(providerId)
									.build();
			memberRepository.save(memberEntity);
		} else {
			logger.info("로그인을 이미 한 적이 있습니다.");
		}
		return new PrincipalDetails(memberEntity, oauth2User.getAttributes());
	}
}
