package com.smartexamsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartexamsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

	public boolean existsByEmail(String email);
}
