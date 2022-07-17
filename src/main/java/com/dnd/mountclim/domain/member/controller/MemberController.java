package com.dnd.mountclim.domain.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.domain.member.entity.MemberEntity;
import com.dnd.mountclim.domain.member.repository.MemberRepository;

@RestController
@RequestMapping("/api/member")
public class MemberController {
	
	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/get_member")
	public String get_member() {
		MemberEntity member = memberRepository.findByUsername("haeyong");
		return "<h1>" + member.getId() + ". " + member.getUsername() + "</h1>";
	}
}
