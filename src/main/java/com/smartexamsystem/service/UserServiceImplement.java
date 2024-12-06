package com.smartexamsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smartexamsystem.entity.User;
import com.smartexamsystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;


@Service
public class UserServiceImplement implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public User saveUser(User user) {
		User newUser=userRepo.save(user);
		return newUser;
	}

	@Override
	public List<User> getAllUser() {
		
		return userRepo.findAll();
	}

	@Override
	public User getUserById(int id) {
		
		return userRepo.findById(id).get();
	}

	@Override
	public boolean deleteUser(int id) {
		User user=userRepo.findById(id).get();
		if(user!=null) {
			userRepo.delete(user);
			return true;
		}
		return false;
	}
	
	public void removeSessionMessage() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");

	}



}
