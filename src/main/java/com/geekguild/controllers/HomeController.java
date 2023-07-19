package com.geekguild.controllers;

import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private UserRepository userDao;
    private PostRepository postDao;

    public HomeController(UserRepository userDao, PostRepository postDao) {
        this.userDao = userDao;
        this.postDao = postDao;
    }
    @GetMapping("/home")
    public String landingPage(Model model) {
        model.addAttribute("post", new Post());
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);


        return "users/home";
    }

    @GetMapping("/about-us")
    public String showGroups() {

        return "/partials/about";
    }


    @PostMapping("/home")
    public String showCreatePostForm(@ModelAttribute Post post) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(loggedInUser);
        postDao.save(post);
        return "redirect:home";
    }


}
