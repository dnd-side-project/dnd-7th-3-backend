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
import com.dnd.mountclim.config.jwt.JwtTokenProvider;
import com.dnd.mountclim.config.oauth.PrincipalOAuth2SuccessHandler;
import com.dnd.mountclim.config.oauth.PrincipalOAuth2UserService;
import com.dnd.mountclim.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CorsFilter corsFilter;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	
	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	@Autowired
	private PrincipalOAuth2SuccessHandler principalOAuth2SuccessHandler;
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
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
		.addFilter(new JwtAuthenticationFilter(jwtTokenProvider, authenticationManager()))
		.addFilter(new JwtAuthorizationFilter(jwtTokenProvider, authenticationManager(), memberRepository))
		.authorizeRequests()
		.antMatchers("/api/member/**").authenticated()	
		.antMatchers("/api/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")	
		.antMatchers("/api/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		.oauth2Login()
		.successHandler(principalOAuth2SuccessHandler)
		.userInfoEndpoint()
		.userService(principalOAuth2UserService);
	}
}
