package com.dnd.mountclim.domain.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dnd.mountclim.domain.member.repository.TestRepository;
import com.dnd.mountclim.domain.member.vo.TestVo;

@Service
public class TestService {
	
	@Autowired
	private TestRepository testRepository;
	
	public TestVo getTest() {
		return testRepository.getTest();
	}
}
