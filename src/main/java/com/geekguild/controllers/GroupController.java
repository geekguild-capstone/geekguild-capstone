
package com.geekguild.controllers;

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
//        User loggedInUser = getCurrentLoggedInUser();
//        Group newGroup = new Group();
//        newGroup.setUserId();
//        return "redirect:/group/{groupId}";
    }

    @PostMapping("/group/{groupId}/join")
    public String joinGroup(@PathVariable Long groupId) {
        User loggedInUser = getCurrentLoggedInUser();
        joinGroup(groupId, loggedInUser);
        return "redirect:/group/" + groupId;
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
//            int groupMembersCount = groupDao.getGroupMembersCount(groupId);
//            model.addAttribute("groupMembersCount", groupMembersCount);
            return "/groups/group";
        }
    }

    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void joinGroup(Long groupId, User user) {
        Group group = groupDao.findById(groupId).orElse(null);
        if (group != null) {
//            group.getMembers().add(user);
            groupDao.save(group);
        }
    }


}



//
//
//package com.geekguild.controllers;
//
//import com.geekguild.models.FriendRequest;
//import com.geekguild.models.Group;
//import com.geekguild.models.Post;
//import com.geekguild.models.User;
//import com.geekguild.repositories.FriendRequestRepository;
//import com.geekguild.repositories.GroupRepository;
//import com.geekguild.repositories.PostRepository;
//import com.geekguild.repositories.UserRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//public class GroupController {
//
//    private final GroupRepository groupDao;
//    private final UserRepository userDao;
//    private final FriendRequestRepository friendDao;
//    private final PostRepository postDao;
//
//    public GroupController(GroupRepository groupDao, UserRepository userDao, FriendRequestRepository friendDao, PostRepository postDao) {
//        this.groupDao = groupDao;
//        this.userDao = userDao;
//        this.friendDao = friendDao;
//        this.postDao = postDao;
//    }
//
//
//    @GetMapping("/groups")
//    public String showGroups(Model model) {
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
//        List<Group> groups = groupDao.findAll();
//        model.addAttribute("groups", groups);
//
//        // Retrieve groups that the logged-in user is a member of
//        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
//
//        // Create a map to store group IDs and their corresponding member counts
//        Map<Long, Integer> groupMembersCount = new HashMap<>();
//
//        // Loop through the groups to calculate and store the member counts
//        for (Group group : loggedInUserGroups) {
//            int membersCount = group.getMembers().size();
//            groupMembersCount.put(group.getId(), membersCount);
//        }
//
//
//        // Add the list of groups and group members count to the model
//        model.addAttribute("loggedInUserGroups", loggedInUserGroups);
//        model.addAttribute("groupMembersCount", groupMembersCount);
//
//
//        return "groups/groups";
//    }
//
//    @PostMapping("/group/{groupId}/join")
//    public String joinGroup(@PathVariable Long groupId) {
////        String username = principal.getName();
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        joinGroup(groupId, loggedInUser);
//        return "redirect:/group/" + groupId;
//    }
//
////    @GetMapping("/group")
////    public String showGroup() {
////
////        return "group";
////    }
//
//    @GetMapping("/group/{id}")
//    public String viewGroup(@PathVariable("id") Long groupId, Model model) {
//        model.addAttribute("post", new Post());
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User user = userDao.getReferenceById(loggedInUser.getId());
//        model.addAttribute("user", user);
////        List<Post> posts = postDao.findAll();
////        model.addAttribute("posts", posts);
//        List<Post> groupPosts = postDao.findByGroupId(groupId);
//        model.addAttribute("groupPosts", groupPosts);
//        List<User> users = userDao.findAll();
//        model.addAttribute("users", users);
//        List<FriendRequest> receiveFriends = friendDao.findByReceiverAndStatus(user, "accepted");
//        model.addAttribute("receiveFriends", receiveFriends);
//        List<FriendRequest> sentFriends = friendDao.findBySenderAndStatus(user, "accepted");
//        model.addAttribute("sentFriends", sentFriends);
//        // Fetch the list of users who are not friends with the logged-in user
//        List<User> usersNotFriendsWithLoggedInUser = userDao.findUsersNotFriendsWithAndNotPending(user.getId());
//        model.addAttribute("notFriends", usersNotFriendsWithLoggedInUser);
//        //Retrieve the group with the giver Id from the database
//        Group group = groupDao.findById(groupId).orElse(null);
//        model.addAttribute("group", group);
//        List<Group> groups = groupDao.findAll();
//        model.addAttribute("groups", groups);
//
//
//
////         Check if the group exists
//        if (group == null) {
//            return "redirect:/error";
//        } else {
//            // Get the count of members in the group using the repository method
//            int groupMembersCount = groupDao.getGroupMembersCount(groupId);
//            model.addAttribute("groupMembersCount", groupMembersCount);
//
//            return "/groups/group";
//        }
//
//
//    }
//
//    public void joinGroup(Long groupId, User user) {
//        Group group = groupDao.findById(groupId).orElse(null);
//        if (group != null) {
//            group.getMembers().add(user);
//            groupDao.save(group);
//        }
//    }
//
//}
//
//
