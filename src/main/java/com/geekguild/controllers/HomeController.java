package com.geekguild.controllers;

import com.geekguild.models.User;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private UserRepository userDao;


    public HomeController(UserRepository userDao) {
        this.userDao = userDao;
    }
    @GetMapping("/home")
    public String landingPage(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);


        return "users/home";
    }

    @GetMapping("/about-us")
    public String showGroups() {
        return "/partials/about";
    }


}
