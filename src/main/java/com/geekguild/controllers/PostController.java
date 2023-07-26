package com.geekguild.controllers;

import com.geekguild.models.Group;
import com.geekguild.models.Post;
import com.geekguild.models.User;
import com.geekguild.repositories.GroupRepository;
import com.geekguild.repositories.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final GroupRepository groupDao;

    public PostController(PostRepository postDao, GroupRepository groupDao) {
        this.postDao = postDao;
        this.groupDao = groupDao;
    }

    @PostMapping("/group/{id}")
    public String showCreatePostForm(@PathVariable long id, @ModelAttribute Post post, @RequestParam("image") String image) {
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

    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}




//package com.geekguild.controllers;
//
//import com.geekguild.models.Group;
//import com.geekguild.models.Post;
//import com.geekguild.models.User;
//import com.geekguild.repositories.GroupRepository;
//import com.geekguild.repositories.PostRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class PostController {
//    private final PostRepository postDao;
//    private final GroupRepository groupDao;
//
//    public PostController(PostRepository postDao, GroupRepository groupDao) {
//        this.postDao = postDao;
//        this.groupDao = groupDao;
//    }
//
//    @PostMapping("/group/{id}")
//    public String showCreatePostForm(@PathVariable long id,@ModelAttribute Post post, @RequestParam("image") String image) {
//        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        post.setUser(loggedInUser);
//        Group group = groupDao.findById(id).get();
//        post.setGroup(group);
//
//        // Set the image URL from the request parameter directly in the post entity
//        post.setImage(image);
//
//        postDao.save(post);
//        return "redirect:/group/{id}";
//    }
//
//}
