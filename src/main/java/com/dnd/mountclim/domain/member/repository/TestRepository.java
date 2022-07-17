package com.dnd.mountclim.domain.member.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dnd.mountclim.domain.member.vo.TestVo;

@Repository
public class TestRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public TestVo getTest() {
		return sqlSession.selectOne("test.getTest");
	}
}
