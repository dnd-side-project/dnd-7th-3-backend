package com.dnd.mountclim.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dnd.mountclim.domain.entity.MemberEntity;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private MemberEntity memberEntity;	// 콤포지션
	private Map<String, Object> attributes;
	
	// 일반 로그인할 때 사용하는 생성자
	public PrincipalDetails(MemberEntity memberEntity) {
		this.memberEntity = memberEntity;
	}
	
	// OAuth 로그인할 때 사용하는 생성자
	public PrincipalDetails(MemberEntity memberEntity, Map<String, Object> attributes) {
		this.memberEntity = memberEntity;
		this.attributes = attributes;
	}

	// 해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return memberEntity.getRole();
			}
		});
		return collect;
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
