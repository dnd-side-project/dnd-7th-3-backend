package com.dnd.mountclim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestApiController {

	//git test
	@GetMapping("/home")
	public String home() {
		return "<h1>home</h1>";
	}
}
