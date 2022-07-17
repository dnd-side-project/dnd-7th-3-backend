package com.dnd.mountclim.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.domain.user.service.TestService;
import com.dnd.mountclim.domain.user.vo.TestVo;

@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@GetMapping("/get_test")
	public String get_test() {
		TestVo test = testService.getTest();
		return "<h1>" + test.getId() + ". " + test.getName() + "</h1>";
	}
}
