
package com.geekguild.controllers;

import java.lang.Long;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class GroupController {

    private final GroupRepository groupDao;
    private final UserRepository userDao;
    private final FriendRequestRepository friendDao;
    private final PostRepository postDao;
    private final CommentRepository commentDao;

    public GroupController(GroupRepository groupDao, UserRepository userDao, FriendRequestRepository friendDao, PostRepository postDao, CommentRepository commentDao) {
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.friendDao = friendDao;
        this.postDao = postDao;
        this.commentDao = commentDao;
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
        model.addAttribute("groups", groupDao.findAll());

        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        Map<Long, Integer> groupMembersCount = new HashMap<>();

        for (Group group : loggedInUserGroups) {
            int membersCount = group.getMembers().size();
            groupMembersCount.put(group.getId(), membersCount);
        }

        model.addAttribute("loggedInUserGroups", loggedInUserGroups);
        model.addAttribute("groupMembersCount", groupMembersCount);

        return "groups/groups";
    }

    @PostMapping("/group/create")
    public String createGroup(@ModelAttribute Group group) {
        User loggedInUser = getCurrentLoggedInUser();
        Group newGroup = new Group();

        newGroup.setGroupname(group.getGroupname()); // Set other properties if required
        newGroup.setImage(group.getImage());
        newGroup.setBanner(group.getBanner());
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
//                group.getMembers().remove(loggedInUser);
//                groupDao.save(group);
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
        User loggedInUser = getCurrentLoggedInUser();
        model.addAttribute("post", new Post());
        model.addAttribute("user", loggedInUser);
        List<Post> groupPosts = postDao.findByGroupId(groupId);
        model.addAttribute("groupPosts", groupPosts);
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("receiveFriends", friendDao.findByReceiverAndStatus(loggedInUser, "accepted"));
        model.addAttribute("sentFriends", friendDao.findBySenderAndStatus(loggedInUser, "accepted"));


        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(loggedInUser.getId());
        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);

        Group group = groupDao.findById(groupId).orElse(null);
        model.addAttribute("group", group);
        model.addAttribute("groups", groupDao.findAll());

        // Fetch the comments related to the groupPosts
        List<List<Comments>> groupPostComments = new ArrayList<>();
        for (Post post : groupPosts) {
            List<Comments> commentsForPost = commentDao.findByPost(post);
            groupPostComments.add(commentsForPost);
        }

        model.addAttribute("groupPostComments", groupPostComments);

        if (group == null) {
            return "redirect:/error";
        } else {
            int groupMembersCount = groupDao.getGroupMembersCount(groupId);
            model.addAttribute("groupMembersCount", groupMembersCount);
            return "/groups/group";
        }
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


}

