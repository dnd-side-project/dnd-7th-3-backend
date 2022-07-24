package com.dnd.mountclim.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberEntity memberEntity = memberRepository.findByUsername(username);
		if(memberEntity != null) {
			return new PrincipalDetails(memberEntity);
		}
		return null;
	}

}
