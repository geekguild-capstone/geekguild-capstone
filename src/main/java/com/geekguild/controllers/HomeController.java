package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserRepository userDao;
    private final PostRepository postDao;
    private final FriendRequestRepository friendDao;
    private final CommentRepository commentDao;

    private final ReactionRepository reactionDao;


    public HomeController(UserRepository userDao, PostRepository postDao, FriendRequestRepository friendDao, CommentRepository commentDao, ReactionRepository reactionDao) {
        this.userDao = userDao;
        this.postDao = postDao;
        this.friendDao = friendDao;
        this.commentDao = commentDao;
        this.reactionDao = reactionDao;
    }


    @GetMapping("/home")
    public String landingPage(Model model) {
        //Get logged in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);

        // add dynamic title page
        model.addAttribute("title", "GeekGuild");

        //Get friend requests
        List<FriendRequest> friendRequests = friendDao.findByReceiverAndStatus(loggedInUser, "pending");
        model.addAttribute("requests", friendRequests);

        //Get ready for new objects
        model.addAttribute("post", new Post());
        model.addAttribute("comment", new Comments());
        model.addAttribute("request", new PostUpdateRequest());
        model.addAttribute("commentRequest", new CommentUpdateRequest());

        // Fetch only the posts where groupId is null and add it to the model
        List<Post> posts = postDao.findByGroupIdIsNull();
        model.addAttribute("posts", posts);

        // Add the reactions to the model, so they can be accessed within the view
        model.addAttribute("reactions", getReactions(posts));

        List<Object[]> postReactionsCounts = reactionDao.countReactionsForPosts(posts);

        // Calculate and add the counts of each type of reaction for each post to the model
        Map<Long, Integer> postLikesCount = new HashMap<>();
        Map<Long, Integer> postLovesCount = new HashMap<>();
        Map<Long, Integer> postLaughsCount = new HashMap<>();

        for (Object[] row : postReactionsCounts) {
            long postId = (Long) row[0];
            String reactionType = (String) row[1];
            int count = ((Number) row[2]).intValue();

            if ("like".equalsIgnoreCase(reactionType)) {
                postLikesCount.put(postId, count);
            } else if ("love".equalsIgnoreCase(reactionType)) {
                postLovesCount.put(postId, count);
            } else if ("laugh".equalsIgnoreCase(reactionType)) {
                postLaughsCount.put(postId, count);
            }
        }

       // Add the post reaction counts to the model
        model.addAttribute("postLikesCount", postLikesCount);
        model.addAttribute("postLovesCount", postLovesCount);
        model.addAttribute("postLaughsCount", postLaughsCount);


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

        // Collect all comments for which we need to fetch reaction counts
        List<Comments> allComments = homePostComments.stream().flatMap(List::stream).collect(Collectors.toList());

        // Fetch comment reaction counts in a single query
        List<Object[]> commentReactionsCounts = reactionDao.countReactionsForComments(allComments);

        // Calculate and add the counts of each type of reaction for each comment to the model
        Map<Long, Integer> commentLikesCount = new HashMap<>();
        Map<Long, Integer> commentLovesCount = new HashMap<>();
        Map<Long, Integer> commentLaughsCount = new HashMap<>();

        for (Object[] row : commentReactionsCounts) {
            long commentId = (Long) row[0];
            String reactionType = (String) row[1];
            int count = ((Number) row[2]).intValue();

            if ("like".equalsIgnoreCase(reactionType)) {
                commentLikesCount.put(commentId, count);
            } else if ("love".equalsIgnoreCase(reactionType)) {
                commentLovesCount.put(commentId, count);
            } else if ("laugh".equalsIgnoreCase(reactionType)) {
                commentLaughsCount.put(commentId, count);
            }
        }

        // Add the comment reaction counts to the model
        model.addAttribute("commentLikesCount", commentLikesCount);
        model.addAttribute("commentLovesCount", commentLovesCount);
        model.addAttribute("commentLaughsCount", commentLaughsCount);

        // List of users not friend with logged in user
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

        // Check if the snippet field is empty (empty string) and set it to null
        if (newComment.getSnippet() != null && newComment.getSnippet().trim().isEmpty()) {
            newComment.setSnippet(null);
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

    private Map<Long, List<Reaction>> getPostReactions(List<Post> posts) {
        Map<Long, List<Reaction>> postReactionsMap = new HashMap<>();
        for (Post post : posts) {
            List<Reaction> reactions = reactionDao.findByPost(post);
            postReactionsMap.put(post.getId(), reactions);
        }
        return postReactionsMap;
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
    private List<Integer> getReactionCounts(List<Post> posts, String reactionType) {
        List<Integer> counts = new ArrayList<>();
        for (Post post : posts) {
            int count = (int) post.getReactions().stream()
                    .filter(reaction -> reaction.getReaction().equalsIgnoreCase(reactionType))
                    .count();
            counts.add(count);
        }
        return counts;
    }


//    private List<Integer> getCommentReactionCounts(List<Comments> comments, String reactionType) {
//        List<Integer> counts = new ArrayList<>();
//        for (Comments comment : comments) {
//            int count = (int) comment.getReactions().stream()
//                    .filter(reaction -> reaction.getReaction().equalsIgnoreCase(reactionType))
//                    .count();
//            counts.add(count);
//        }
//        return counts;
//    }

}

