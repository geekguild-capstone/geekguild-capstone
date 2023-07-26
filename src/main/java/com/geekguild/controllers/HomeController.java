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
        model.addAttribute("posts", postDao.findAll());
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

    @PostMapping("/home/remove")
    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User friend = userDao.findById(friendId).orElse(null);

        if (friend != null) {
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
}





//package com.geekguild.controllers;
//
//import com.geekguild.models.FriendRequest;
//import com.geekguild.models.Post;
//import com.geekguild.models.User;
//import com.geekguild.repositories.FriendRequestRepository;
//import com.geekguild.repositories.PostRepository;
//import com.geekguild.repositories.UserRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.ui.Model;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//public class HomeController {
//
//    private UserRepository userDao;
//    private PostRepository postDao;
//    private FriendRequestRepository friendDao;
//
//
//    public HomeController(UserRepository userDao, PostRepository postDao, FriendRequestRepository friendDao) {
//        this.userDao = userDao;
//        this.postDao = postDao;
//        this.friendDao = friendDao;
//    }
//
//    @GetMapping("/home")
//    public String landingPage(Model model) {
//        model.addAttribute("post", new Post());
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userDao.getReferenceById(loggedInUser.getId());
//        model.addAttribute("user", user);
//        List<Post> posts = postDao.findAll();
//        model.addAttribute("posts", posts);
//        List<User> users = userDao.findAll();
//        model.addAttribute("users", users);
//        List<FriendRequest> receiveFriends = friendDao.findByReceiverAndStatus(user, "accepted");
//        model.addAttribute("receiveFriends", receiveFriends);
//        List<FriendRequest> sentFriends = friendDao.findBySenderAndStatus(user, "accepted");
//        model.addAttribute("sentFriends", sentFriends);
//        // Fetch the list of users who are not friends with the logged-in user
//        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(user.getId());
//        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);
//
//
//
//        return "users/home";
//    }
//
//    @GetMapping("/about-us")
//    public String showAbout() {
//
//        return "/partials/about-us";
//    }
//
//    @PostMapping("/home")
//    public String showCreatePostForm(@ModelAttribute Post post, @RequestParam("image") String image) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        post.setUser(loggedInUser);
//
//        // Set the image URL from the request parameter directly in the post entity
//        post.setImage(image);
//
//        postDao.save(post);
//        return "redirect:/home";
//    }
//
//    @PostMapping("/home/add")
//    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User receiver = userDao.findById(receiverId).orElse(null);
//
//        if (receiver != null) {
//            // Create a new FriendRequest and set the sender, receiver, and status
//            FriendRequest friendRequest = new FriendRequest();
//            friendRequest.setSender(loggedInUser);
//            friendRequest.setReceiver(receiver);
//            friendRequest.setStatus("pending"); // You can set the initial status as "pending" or any other value
//
//            // Save the friend request to the database
//            friendDao.save(friendRequest);
//        }
//
//        return "redirect:/home"; // Redirect back to the home page after sending the friend request
//    }
//
//
//    @PostMapping("/home/remove")
//    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
//        System.out.println("Endpoint hit");
//
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User friend = userDao.findById(friendId).orElse(null);
//
//        if (friend != null) {
//            if ("sender".equals(loggedInUserType)) {
//                // If the logged-in user is the sender of the friend request
//                FriendRequest friendRequest = friendDao.findBySenderAndReceiverAndStatus(loggedInUser, friend, "accepted");
//                if (friendRequest != null) {
//                    // Set the status of the friend request to "rejected"
//                    friendRequest.setStatus("rejected");
//                    friendDao.save(friendRequest);
//                    System.out.println("Friend request status set to 'rejected' for sender"); // Add this line for debugging
//                }
//            } else if ("receiver".equals(loggedInUserType)) {
//                // If the logged-in user is the receiver of the friend request
//                FriendRequest friendRequest = friendDao.findBySenderAndReceiverAndStatus(friend, loggedInUser, "accepted");
//                if (friendRequest != null) {
//                    // Set the status of the friend request to "rejected"
//                    friendRequest.setStatus("rejected");
//                    friendDao.save(friendRequest);
//                    System.out.println("Friend request status set to 'rejected' for receiver"); // Add this line for debugging
//                }
//            }
//        }
//
//        return "redirect:/home";
//    }
//
//
//}
