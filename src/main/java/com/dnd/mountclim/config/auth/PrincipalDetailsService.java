package com.dnd.mountclim.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	private static Logger logger = LoggerFactory.getLogger(PrincipalDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("[ PrincipalDetailsService의 loadUserByUsername() 진입 ]");
		MemberEntity memberEntity = memberRepository.findByUsername(username);
		logger.info("[ memberEntity : " + memberEntity + "]");
		if(memberEntity == null) {
			throw new UsernameNotFoundException("memberEntity가 존재하지 않습니다.");
		}
		return new PrincipalDetails(memberEntity);
	}
}
