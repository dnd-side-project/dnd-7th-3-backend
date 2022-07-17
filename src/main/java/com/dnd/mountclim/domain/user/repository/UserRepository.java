package com.dnd.mountclim.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.mountclim.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
}
