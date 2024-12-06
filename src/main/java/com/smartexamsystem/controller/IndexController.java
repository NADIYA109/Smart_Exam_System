package com.smartexamsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index"; // Returns the name of the HTML file without extension
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Returns the name of the HTML file without extension
    }


    @GetMapping("/register")
    public String showRegistrationPage() {
        return "registration"; //  registration page is named registration.html
    }
}
