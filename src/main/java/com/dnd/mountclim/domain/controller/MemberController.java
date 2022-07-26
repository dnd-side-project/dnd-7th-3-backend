package com.dnd.mountclim.domain.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@GetMapping("/member")
	public String member(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		logger.info("principal : " + principal.getMemberEntity().getId());
		logger.info("principal : " + principal.getMemberEntity().getUsername());
		logger.info("principal : " + principal.getMemberEntity().getPassword());
		
		return "<h1>member</h1>";
	}
	
	@GetMapping("/manager/reports")
	public String reports() {
		return "<h1>reports</h1>";
	}
	
	@GetMapping("/admin/members")
	public List<MemberEntity> members() {
		return memberRepository.findAll();
	}
	
	@PostMapping("/join")
	public String join(@RequestBody MemberEntity memberEntity) {
		memberEntity.setPassword(bCryptPasswordEncoder.encode(memberEntity.getPassword()));
		memberEntity.setRole("ROLE_USER");
		memberRepository.save(memberEntity);
		return "회원가입완료";
	}	
}
