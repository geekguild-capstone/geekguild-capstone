package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.CommentRepository;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final FriendRequestRepository friendDao;
    private final CommentRepository commentDao;




    public HomeController(UserRepository userDao, PostRepository postDao, FriendRequestRepository friendDao, CommentRepository commentDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.friendDao = friendDao;
        this.commentDao = commentDao;
    }


    @GetMapping("/home")
    public String landingPage(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("comment", new Comments());

        // add dynamic title page
        model.addAttribute("title", "GeekGuild");

        List<FriendRequest> friendRequests = friendDao.findByReceiverAndStatus(loggedInUser, "pending");
        model.addAttribute("requests", friendRequests);

        model.addAttribute("user", user);
        model.addAttribute("post", new Post());

        // Fetch only the posts where groupId is null
        List<Post> posts = postDao.findByGroupIdIsNull();
        model.addAttribute("posts", posts);

        // Add the reactions to the model, so they can be accessed within the view
        model.addAttribute("reactions", getReactions(posts));

        // Calculate and add the counts of each type of reaction to the model
        List<Integer> likesCounts = getReactionCounts(posts, "like");
        List<Integer> lovesCounts = getReactionCounts(posts, "love");
        List<Integer> laughsCounts = getReactionCounts(posts, "laugh");

        model.addAttribute("likesCount", likesCounts);
        model.addAttribute("lovesCount", lovesCounts);
        model.addAttribute("laughsCount", laughsCounts);

        model.addAttribute("users", userDao.findAll());
        // The friend-related attributes are removed from here
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(loggedInUser, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(loggedInUser, "accepted"));

        // Fetch the comments related to the homePosts
        List<List<Comments>> homePostComments = new ArrayList<>();
        for (Post post : posts) {
            List<Comments> commentsForPost = commentDao.findByPost(post);
            homePostComments.add(commentsForPost);
        }

        model.addAttribute("homePostComments", homePostComments);
        model.addAttribute("request", new PostUpdateRequest());


        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(loggedInUser.getId());
        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);

        return "users/home";
    }

    @GetMapping("/about-us")
    public String showAbout(Model model) {

        model.addAttribute("title", "GeekGuild - Unite, Collaborate, Geek Out!");
        return "/partials/about-us";
    }

    @PostMapping("/friends/{requestId}/accept")
    public String acceptFriendRequest(@PathVariable Long requestId) {
        updateFriendRequestStatus(requestId, "accepted");
        return "redirect:/home";
    }

    @PostMapping("/friends/{requestId}/reject")
    public String rejectFriendRequest(@PathVariable Long requestId) {
        updateFriendRequestStatus(requestId, "rejected");
        return "redirect:/home";
    }

    @PostMapping("/friends/add")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId) {
        User loggedInUser = getLoggedInUser();
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

    @PostMapping("/friends/remove")
    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
        Long loggedInUserId = getUserIdFromSecurityContext();
        User loggedInUser = userDao.findById(loggedInUserId).orElse(null);
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

    @PostMapping("/comment")
    public String addComment(@ModelAttribute("newComment") Comments newComment) {
        // Get the logged-in user
        User loggedInUser = getLoggedInUser();

        // Fetch the post based on the provided postId in the newComment object
        Post post = postDao.findById(newComment.getPost().getId()).orElse(null);
        if (post == null) {
            // Invalid post
            return "redirect:/home"; // Redirect to your home page
        }

        // Set the user and post for the new comment
        newComment.setUser(loggedInUser);
        newComment.setPost(post);

        // Save the new comment to the database
        commentDao.save(newComment);

        // Redirect back to the home page
        return "redirect:/home";
    }


    private User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Other post-related methods and endpoints can stay here
    // ...
    private List<List<Reaction>> getReactions(List<Post> posts) {
        List<List<Reaction>> reactions = new ArrayList<>();
        for (Post post : posts) {
            reactions.add(post.getReactions());
        }
        return reactions;
    }


    private void updateFriendRequestStatus(Long requestId, String status) {
        Optional<FriendRequest> optionalFriendRequest = friendDao.findById(requestId);
        optionalFriendRequest.ifPresent(friendRequest -> {
            friendRequest.setStatus(status);
            friendDao.save(friendRequest);
        });
    }

    private Long getUserIdFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        return null;
    }




    // Helper method to calculate the counts of each type of reaction for all posts
    private List<Integer> getReactionCounts(List<Post> posts, String reactionId) {
        List<Integer> counts = new ArrayList<>();
        for (Post post : posts) {
            int count = (int) post.getReactions().stream()
                    .filter(reaction -> reaction.getReaction().equalsIgnoreCase(reactionId))
                    .count();
            counts.add(count);
        }
        return counts;
    }

}

