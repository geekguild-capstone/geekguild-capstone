
package com.geekguild.controllers;

import java.lang.Long;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GroupController {

    private final GroupRepository groupDao;
    private final UserRepository userDao;
    private final FriendRequestRepository friendDao;
    private final PostRepository postDao;
    private final CommentRepository commentDao;
    private final ReactionRepository reactionDao;

    public GroupController(GroupRepository groupDao, UserRepository userDao, FriendRequestRepository friendDao, PostRepository postDao, CommentRepository commentDao, ReactionRepository reactionDao) {
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.friendDao = friendDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.reactionDao = reactionDao;
    }

    @GetMapping("/groups")
    public String showGroups(Model model) {
        model.addAttribute("group", new Group()); // Add an empty Group object to the model
        User loggedInUser = getCurrentLoggedInUser();
        model.addAttribute("post", new Post());
        model.addAttribute("user", loggedInUser);
        model.addAttribute("posts", postDao.findAll());
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(loggedInUser, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(loggedInUser, "accepted"));

        model.addAttribute("title", "GeekGuild - Groups");

        // Get groups that the loggedInUser is not a part of using the custom query
        List<Group> loggedInUserNotPartOfGroups = groupDao.findGroupsNotContainingMember(loggedInUser);
        model.addAttribute("suggestedGroups", loggedInUserNotPartOfGroups);

        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        Map<Long, Integer> groupMembersCount = new HashMap<>();

        for (Group group : loggedInUserGroups) {
            int membersCount = group.getMembers().size();
            groupMembersCount.put(group.getId(), membersCount);
        }

        model.addAttribute("loggedInUserGroups", loggedInUserGroups);
        model.addAttribute("groupMembersCount", groupMembersCount);

        //Get logged in users groups for the navbar
        List<Group> loggedInUserGroupsNav = groupDao.findByMembersContaining(loggedInUser);
        model.addAttribute("listGroups", loggedInUserGroupsNav);

        return "groups/groups";
    }


    @PostMapping("/group/create")
    public String createGroup(@ModelAttribute Group group,@RequestParam("fileURLGroupBanner") String banner, @RequestParam("fileURLGroupImage") String image ) {
        User loggedInUser = getCurrentLoggedInUser();
        Group newGroup = new Group();
        System.out.println(banner);

        newGroup.setGroupname(group.getGroupname()); // Set other properties if required
        newGroup.setImage(image);
        newGroup.setBanner(banner);
        newGroup.setDescription(group.getDescription());
        newGroup.setAdminId(loggedInUser.getId());


        // Add the logged-in user as a member of the new group
        Set<User> members = new HashSet<>();
        members.add(loggedInUser);
        newGroup.setMembers(members);

        // Save the new group
        groupDao.save(newGroup);

        return "redirect:/groups";
    }

    @PostMapping("/group/{groupId}/join")
    public String joinGroup(@PathVariable Long groupId) {
        User loggedInUser = getCurrentLoggedInUser();
        joinGroup(groupId, loggedInUser);
        return "redirect:/group/" + groupId;
    }

    // Method to delete posts and their comments by user in a group
    private void deletePostsAndCommentsByUserInGroup(Long groupId, User user) {
        List<Post> posts = postDao.findByGroupIdAndUser(groupId, user);
        for (Post post : posts) {
            // Remove comments related to the post
            List<Comments> comments = commentDao.findByPost(post);
            commentDao.deleteAll(comments);
        }
        postDao.deleteAll(posts);
    }

    // Method to delete posts and their comments by group ID
    private void deletePostsAndCommentsByGroupId(Long groupId) {
        List<Post> posts = postDao.findByGroupId(groupId);
        for (Post post : posts) {
            // Remove comments related to the post
            List<Comments> comments = commentDao.findByPost(post);
            commentDao.deleteAll(comments);

            // Set the group_id to null for the post to avoid constraint violation
            post.setGroup(null);
            postDao.save(post);
        }
        postDao.deleteAll(posts);
    }

    // Add this mapping for leaving a group
    @PostMapping("/group/{groupId}/leave")
    public String leaveGroup(@PathVariable Long groupId) {
        User loggedInUser = getCurrentLoggedInUser();
        System.out.println("Leave Group method invoked for groupId: " + groupId);

        Group group = groupDao.findById(groupId).orElse(null);

        if (group != null) {
            if (group.getAdminId() != null && group.getAdminId().equals(loggedInUser.getId())) {
                // User is the admin, so remove the posts, remove members, and delete the group
                deletePostsAndCommentsByGroupId(groupId);
                group.getMembers().clear();
                groupDao.delete(group);
            } else {
                // User is not the admin, so just remove the user from the group
                User userToRemove = groupDao.findMemberById(groupId, loggedInUser.getId());
                if (userToRemove != null) {
                    // Delete posts and their comments made by the user in the group
                    deletePostsAndCommentsByUserInGroup(groupId, loggedInUser);

                    // Remove the user from the group
                    group.getMembers().remove(userToRemove);
                    groupDao.save(group);
                }
            }
        }


        return "redirect:/groups";
    }


    @GetMapping("/group/{id}")
    public String viewGroup(@PathVariable("id") Long groupId, Model model) {
        //Get logged in user
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);

        // Fetch the group members and remove the currently signed-in user from the list
        List<User> groupMembers = groupDao.findGroupMembersById(groupId);
        groupMembers.remove(user);
        model.addAttribute("groupMembers", groupMembers);

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

        // Fetch only the posts for the group Id and add it to the model
        List<Post> groupPosts = postDao.findByGroupId(groupId);
        model.addAttribute("posts", groupPosts);

        // Add the reactions to the model, so they can be accessed within the view
        model.addAttribute("reactions", getReactions(groupPosts));

        List<Object[]> postReactionsCounts = reactionDao.countReactionsForPosts(groupPosts);

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

//        model.addAttribute("users", userDao.findAll());
        //Get Friends and add it to model
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(loggedInUser, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(loggedInUser, "accepted"));

        // Fetch the comments related to the groupPosts
        List<List<Comments>> groupPostComments = new ArrayList<>();
        for (Post post : groupPosts) {
            List<Comments> commentsForPost = commentDao.findByPost(post);
            groupPostComments.add(commentsForPost);
        }

        model.addAttribute("groupPostComments", groupPostComments);

        // Collect all comments for which we need to fetch reaction counts
        List<Comments> allComments = groupPostComments.stream().flatMap(List::stream).collect(Collectors.toList());

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



//        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(loggedInUser.getId());
//        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);

        Group group = groupDao.findById(groupId).orElse(null);
        model.addAttribute("group", group);
        model.addAttribute("groups", groupDao.findAll());

        //Get logged in users groups for the navbar
        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        model.addAttribute("listGroups", loggedInUserGroups);





//        // Calculate and add the counts of each type of reaction to the model
//        List<Integer> likesCounts = getReactionCounts(groupPosts, "like");
//        List<Integer> lovesCounts = getReactionCounts(groupPosts, "love");
//        List<Integer> laughsCounts = getReactionCounts(groupPosts, "laugh");
//
//        model.addAttribute("likesCount", likesCounts);
//        model.addAttribute("lovesCount", lovesCounts);
//        model.addAttribute("laughsCount", laughsCounts);

        if (group == null) {
            return "redirect:/error";
        } else {
            int groupMembersCount = groupDao.getGroupMembersCount(groupId);
            model.addAttribute("groupMembersCount", groupMembersCount);
            return "/groups/group";
        }
    }

    @PostMapping("/group/{groupId}/comment")
    public String addComment(@PathVariable Long groupId, @ModelAttribute("newComment") Comments newComment) {
        // Get the logged-in user
        User loggedInUser = getCurrentLoggedInUser();

        // Fetch the post based on the provided postId
        Post post = postDao.findById(newComment.getPost().getId()).orElse(null);
        if (post == null || post.getGroup() == null || !groupId.equals(post.getGroup().getId())) {
            // Invalid post or post doesn't belong to the group
            return "redirect:/group/" + groupId;
        }

        // Set the user and post for the new comment
        newComment.setUser(loggedInUser);
        newComment.setPost(post);

        // Save the new comment to the database
        commentDao.save(newComment);

        // Redirect back to the group's page
        return "redirect:/group/" + groupId;
    }


    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void joinGroup(Long groupId, User user) {
        Group group = groupDao.findById(groupId).orElse(null);
        if (group != null) {
            group.getMembers().add(user);
            groupDao.save(group);
        }
    }

    private List<List<Reaction>> getReactions(List<Post> posts) {
        List<List<Reaction>> reactions = new ArrayList<>();
        for (Post post : posts) {
            reactions.add(post.getReactions());
        }
        return reactions;
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

    // Helper method to escape HTML entities in the code for display
    private String escapeHTML(String code) {
        return code.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // Method to display code on the "/group/{id}" page
    private void displayCodeOnGroupPage(Model model, String code) {
        model.addAttribute("escapedCode", escapeHTML(code));
    }


}

