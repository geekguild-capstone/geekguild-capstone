package com.geekguild.controllers;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(user, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(user, "accepted"));
        model.addAttribute("notFriends", userDao.findUsersNotFriendsWithAndNotPending(user.getId()));

        return "users/home";
    }

    @GetMapping("/about-us")
    public String showAbout() {
        return "/partials/about-us";
    }

    @PostMapping("/home")
    public String showCreatePostForm(@ModelAttribute Post post, @RequestParam("image") String image) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(loggedInUser);
        post.setImage(image);
        postDao.save(post);
        return "redirect:/home";
    }

    @PostMapping("/home/add")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User receiver = userDao.findById(receiverId).orElse(null);

        if (receiver != null) {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setSender(loggedInUser);
            friendRequest.setReceiver(receiver);
            friendRequest.setStatus("pending");
            friendDao.save(friendRequest);
        }

        return "redirect:/home";
    }

//    @PostMapping("/home/remove")
//    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User friend = userDao.findById(friendId).orElse(null);
//
//        if (friend != null) {
//            FriendRequest friendRequest;
//            if ("sender".equals(loggedInUserType)) {
//                friendRequest = friendDao.findBySenderAndReceiverAndStatus(loggedInUser, friend, "accepted");
//            } else if ("receiver".equals(loggedInUserType)) {
//                friendRequest = friendDao.findBySenderAndReceiverAndStatus(friend, loggedInUser, "accepted");
//            } else {
//                return "redirect:/home";
//            }
//
//            if (friendRequest != null) {
//                friendRequest.setStatus("rejected");
//                friendDao.save(friendRequest);
//            }
//        }
//
//        return "redirect:/home";
//    }

    @PostMapping("/home/remove")
    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
        // Get the logged-in user ID from the SecurityContextHolder
        Long loggedInUserId = getUserIdFromSecurityContext();

        // Fetch the logged-in user from the database
        User loggedInUser = userDao.findById(loggedInUserId).orElse(null);

        // Fetch the friend to be removed from the database
        User friend = userDao.findById(friendId).orElse(null);

        if (loggedInUser != null && friend != null) {
            FriendRequest friendRequest;
            if ("sender".equals(loggedInUserType)) {
                friendRequest = friendDao.findBySenderAndReceiverAndStatus(loggedInUser, friend, "accepted");
            } else if ("receiver".equals(loggedInUserType)) {
                friendRequest = friendDao.findBySenderAndReceiverAndStatus(friend, loggedInUser, "accepted");
            } else {
                return "redirect:/home";
            }

            if (friendRequest != null) {
                friendRequest.setStatus("rejected");
                friendDao.save(friendRequest);
            }
        }

        return "redirect:/home";
    }

    // Helper method to get the logged-in user ID from the SecurityContextHolder
    private Long getUserIdFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        return null;
    }
}

