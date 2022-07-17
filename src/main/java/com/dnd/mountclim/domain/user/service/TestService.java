package com.dnd.mountclim.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.user.repository.TestRepository;
import com.dnd.mountclim.domain.user.vo.TestVo;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepository;
	
	public TestVo getTest() {
		return testRepository.getTest();
	}
}
