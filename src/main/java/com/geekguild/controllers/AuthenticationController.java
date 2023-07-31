package com.geekguild.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/login")
    public String showLoginForm(Model model) {

        model.addAttribute("title", "GeekGuild - log in or sign up");


        return "users/login";
    }

}
