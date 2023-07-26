package com.geekguild.controllers;

import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final FriendRequestRepository friendDao;


    public HomeController(UserRepository userDao, PostRepository postDao, FriendRequestRepository friendDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.friendDao = friendDao;
    }

    @GetMapping("/home")
    public String landingPage(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());

        model.addAttribute("user", user);
        model.addAttribute("post", new Post());
        // Fetch only the posts where groupId is null
        List<Post> posts = postDao.findByGroupIdIsNull();
        model.addAttribute("posts", posts);

        model.addAttribute("users", userDao.findAll());
        // The friend-related attributes are removed from here
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(loggedInUser, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(loggedInUser, "accepted"));


        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(loggedInUser.getId());
        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);

        return "users/home";
    }

    @GetMapping("/about-us")
    public String showAbout() {
        return "/partials/about-us";
    }


    // Other post-related methods and endpoints can stay here
    // ...
}

