package com.geekguild.controllers;

import com.geekguild.models.Comments;
import com.geekguild.models.Group;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.GroupRepository;
import com.geekguild.repositories.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final GroupRepository groupDao;

    public PostController(PostRepository postDao, GroupRepository groupDao) {
        this.postDao = postDao;
        this.groupDao = groupDao;
    }

    @PostMapping("/group/{id}/createPost")
    public String createPost(@PathVariable long id, @ModelAttribute Post post, @RequestParam("image") String image) {
        User loggedInUser = getCurrentLoggedInUser();
        post.setUser(loggedInUser);

        Group group = groupDao.findById(id).orElse(null);
        if (group == null) {
            // Handle invalid group ID
            return "redirect:/error";
        }

        post.setGroup(group);
        post.setImage(image);

        postDao.save(post);
        return "redirect:/group/{id}";
    }

    @PostMapping("/group/{groupId}/addComment/{postId}")
    public String addComment(@PathVariable long groupId, @PathVariable long postId, @ModelAttribute Comments comment) {
        User loggedInUser = getCurrentLoggedInUser();
        comment.setUser(loggedInUser);

        Post post = postDao.findById(postId).orElse(null);
        if (post == null || post.getGroup().getId() != groupId) {
            // Handle invalid post ID or post not belonging to the group
            return "redirect:/error";
        }

        comment.setPost(post);
        post.getComments().add(comment);

        postDao.save(post);
        return "redirect:/group/{groupId}";
    }

    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
