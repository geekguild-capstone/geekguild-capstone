package com.geekguild.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

        @GetMapping("/profile")
        public String landingPage() {
            return "/users/profile";
        }

        @GetMapping("/profile/edit")
        public String showGroups() {
            return "/users/edit";
        }






    }
