package com.dnd.mountclim.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.dnd.mountclim.config.jwt.JwtAuthenticationFilter;
import com.dnd.mountclim.config.jwt.JwtAuthorizationFilter;
import com.dnd.mountclim.config.oauth.PrincipalOAuth2UserService;
import com.dnd.mountclim.domain.repository.MemberRepository;

@Configuration
@EnableWebSecurity															// 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)	// @Secured 어노테이션 활성화, @PreAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CorsFilter corsFilter;
	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	
	private MemberRepository memberRepository;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(corsFilter)
			.formLogin().disable()
			.httpBasic().disable()
			.addFilter(new JwtAuthenticationFilter(authenticationManager()))
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
			.authorizeRequests()
			.antMatchers("/user/**").authenticated()	
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")	
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.oauth2Login()
			.userInfoEndpoint()
			.userService(principalOAuth2UserService);
	}
}
