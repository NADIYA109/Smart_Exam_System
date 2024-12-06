package com.smartexamsystem.service;

import java.util.List;

import com.smartexamsystem.entity.User;

public interface UserService {

	
	public  User  saveUser(User user);

	public List<User> getAllUser();

	public User getUserById(int id);

	public boolean deleteUser(int id);
}
