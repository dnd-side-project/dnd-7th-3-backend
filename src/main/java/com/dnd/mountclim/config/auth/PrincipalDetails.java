package com.dnd.mountclim.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dnd.mountclim.domain.entity.MemberEntity;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private MemberEntity memberEntity;
	private Map<String, Object> attributes;
	
	private static Logger logger = LoggerFactory.getLogger(PrincipalDetails.class);
	
	public PrincipalDetails(MemberEntity memberEntity) {
		this.memberEntity = memberEntity;
	}
	
	public PrincipalDetails(MemberEntity memberEntity, Map<String, Object> attributes) {
		this.memberEntity = memberEntity;
		this.attributes = attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		memberEntity.getRoleList().forEach(r -> {
			logger.info("memberEntity role : " + r);
			authorities.add(() -> r);
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return memberEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return memberEntity.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 우리 사이트 1년 동안 회원이 로그인을 안하면 휴먼 계정으로 하기로 한다.
		// 현재시간 - 로그인시간 => 1년을 초과하면 return false;
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return (String) attributes.get("sub");
	}
}
