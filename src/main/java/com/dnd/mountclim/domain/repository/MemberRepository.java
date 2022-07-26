package com.dnd.mountclim.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.mountclim.domain.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
	public MemberEntity findByUsername(String username);
}
