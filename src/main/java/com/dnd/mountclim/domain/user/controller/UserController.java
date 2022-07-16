package com.dnd.mountclim.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnd.mountclim.domain.user.model.User;
import com.dnd.mountclim.domain.user.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/get_user")
	public String get_user() {
		User user = userRepository.findByUsername("haeyong");
		return "<h1>" + user.getId() + ". " + user.getUsername() + "</h1>";
	}
}
