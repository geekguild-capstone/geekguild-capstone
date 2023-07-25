package com.geekguild.controllers;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.Group;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.GroupRepository;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class GroupController {

    private final GroupRepository groupDao;
    private final UserRepository userDao;
    private final FriendRequestRepository friendDao;
    private final PostRepository postDao;

    public GroupController(GroupRepository groupDao, UserRepository userDao, FriendRequestRepository friendDao, PostRepository postDao) {
        this.groupDao = groupDao;
        this.userDao = userDao;
        this.friendDao = friendDao;
        this.postDao = postDao;
    }


    @GetMapping("/groups")
    public String showGroups(Model model) {
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
        return "groups/groups";
    }

    @PostMapping("/group/{groupId}/join")
    public String joinGroup(@PathVariable Long groupId) {
//        String username = principal.getName();
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        joinGroup(groupId, loggedInUser);
        return "redirect:/group/" + groupId;
    }

    @GetMapping("/group")
    public String showGroup() {

        return "groups/group";
    }

    @GetMapping("/group/{id}")
    public String viewGroup(@PathVariable("id") Long groupId, Model model) {
        //Retrieve the group with the giver Id from the database
        Group group = groupDao.findById(groupId).orElse(null);
        model.addAttribute("group", group);

        // CHeck if the group exists
        if (group == null) {
            return "redirect:/error";
        } else {
            return "groups/group";
        }
    }

    public void joinGroup(Long groupId, User user) {
        Group group = groupDao.findById(groupId).orElse(null);
        if (group != null) {
            group.getMembers().add(user);
            groupDao.save(group);
        }
    }

}


