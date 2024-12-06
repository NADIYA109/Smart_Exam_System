package com.smartexamsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartexamsystem.UserService;
import com.smartexamsystem.entity.User;
import com.smartexamsystem.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegistrationController {

	@Autowired
    private final UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bp;

	@Autowired
    private final UserService userService;

    @Autowired
    public RegistrationController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, HttpSession session ,Model model) {

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {

			model.addAttribute("emailError", "Email is already registered");
            return "registration";
        }
        System.out.println(user);
        user.setPassword(bp.encode(user.getPassword()));
      user.setRole("ROLE_USER");
      //user.setRole("ROLE_SME");

        userRepository.save(user);

        userService.performAction();
        session.setAttribute("message", "User Registered Successfully");
        return "redirect:/"; // Redirect to the homepage
    }
}