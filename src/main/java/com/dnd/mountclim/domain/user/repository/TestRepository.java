package com.dnd.mountclim.domain.user.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dnd.mountclim.domain.user.model.Test;

@Repository
public class TestRepository {

	@Autowired
	private SqlSession sqlSession;
	
	public Test getTest() {
		return sqlSession.selectOne("test.getTest");
	}
}
