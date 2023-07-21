package com.geekguild.controllers;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private UserRepository userDao;
    private PostRepository postDao;
    private FriendRequestRepository friendDao;


    public HomeController(UserRepository userDao, PostRepository postDao, FriendRequestRepository friendDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.friendDao = friendDao;
    }

    @GetMapping("/home")
    public String landingPage(Model model) {
        model.addAttribute("post", new Post());
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);
        List<Post> posts = postDao.findAll();
        model.addAttribute("posts", posts);
        List<User> users = userDao.findAll();
        model.addAttribute("users", users);
        List<FriendRequest> receiveFriends = friendDao.findByReceiverAndStatus(user, "accepted");
        model.addAttribute("receiveFriends", receiveFriends);
        List<FriendRequest> sentFriends = friendDao.findBySenderAndStatus(user, "accepted");
        model.addAttribute("sentFriends", sentFriends);




        return "users/home";
    }

    @GetMapping("/about-us")
    public String showGroups() {

        return "/partials/about";
    }

    @PostMapping("/home")
    public String showCreatePostForm(@ModelAttribute Post post, @RequestParam("image") String image) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(loggedInUser);

        // Set the image URL from the request parameter directly in the post entity
        post.setImage(image);

        postDao.save(post);
        return "redirect:/home";
    }

}
