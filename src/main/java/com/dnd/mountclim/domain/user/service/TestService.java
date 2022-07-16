package com.dnd.mountclim.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.user.model.Test;
import com.dnd.mountclim.domain.user.repository.TestRepository;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepository;
	
	public Test getTest() {
		return testRepository.getTest();
	}
}
