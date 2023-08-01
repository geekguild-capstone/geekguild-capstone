package com.geekguild.controllers;

import com.geekguild.models.Reaction;
import com.geekguild.models.User;
import com.geekguild.repositories.PostRepository;
import com.geekguild.repositories.ReactionRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReactionController {

    private final ReactionRepository reactionDao;
    private final PostRepository postDao;
    private final UserRepository userDao;

    @Autowired
    public ReactionController(ReactionRepository reactionDao, PostRepository postDao, UserRepository userDao) {
        this.reactionDao = reactionDao;
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @PostMapping("reaction/submit")
    public String submitReaction(
            @RequestParam("postId") Long postId,
            @RequestParam("reaction") String reaction) {
        System.out.println(postId);
        System.out.println(reaction);
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        Reaction newReaction = new Reaction();
        newReaction.setReaction(reaction);
        newReaction.setPost(postDao.getReferenceById(postId));
        newReaction.setUser(user);
        reactionDao.save(newReaction);

        return "redirect:/home";
    }

}
