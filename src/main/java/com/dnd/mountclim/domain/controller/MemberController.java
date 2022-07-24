package com.dnd.mountclim.domain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dnd.mountclim.config.auth.PrincipalDetails;
import com.dnd.mountclim.domain.entity.MemberEntity;
import com.dnd.mountclim.domain.repository.MemberRepository;

@Controller
public class MemberController {
	
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping({"", "/"})
	public String index() {
		return "index";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@GetMapping("/member")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		logger.info("principalDetails: " + principalDetails.getMemberEntity());
		return "member";
	}
	
	@PostMapping("/join")
	public String join(MemberEntity memberEntity) {
		memberEntity.setRole("ROLE_USER");
		String rawPassword = memberEntity.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		memberEntity.setPassword(encPassword);
		memberRepository.save(memberEntity);
		return "redirect:/loginForm";
	}
}
