package com.geekguild.controllers;

import com.geekguild.models.User;
import com.geekguild.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {


    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        System.out.println(user);
        return "redirect:/login";
        //register page login button taking me to about us/homepage.
        //homepage -profile button -GROUPS button -add friend -
        //groups page - button to group(GROUPS W/ID)


    }


    @PostMapping("/profile/{id}/delete")
    public String deleteProfile(@PathVariable long id) {
        userDao.deleteById(id);
        return "redirect:/users/register";
    }

}