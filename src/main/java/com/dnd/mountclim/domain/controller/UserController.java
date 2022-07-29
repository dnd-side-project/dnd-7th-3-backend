package com.dnd.mountclim.domain.controller;


import com.dnd.mountclim.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dnd.mountclim.domain.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/mybatis")
	public ResponseEntity<?> getUser() {
		return userService.getUser();
	}

	@GetMapping("/jpa")
	public ResponseEntity<?> getUser2() {
		return userService.getUser2();
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
		return userService.save(userDto);
	}
}
