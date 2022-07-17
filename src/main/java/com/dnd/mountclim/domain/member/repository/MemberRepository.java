package com.dnd.mountclim.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.mountclim.domain.member.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

	public MemberEntity findByUsername(String username);
}
