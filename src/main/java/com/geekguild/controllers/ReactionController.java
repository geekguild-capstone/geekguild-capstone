package com.geekguild.controllers;

import com.geekguild.models.Reaction;
import com.geekguild.models.User;
import com.geekguild.repositories.CommentRepository;
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
    private final CommentRepository commentDao;

    @Autowired
    public ReactionController(ReactionRepository reactionDao, PostRepository postDao, UserRepository userDao, CommentRepository commentDao) {
        this.reactionDao = reactionDao;
        this.postDao = postDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    @PostMapping("/reaction/post/submit")
    public String submitReaction(
            @RequestParam("postId") Long postId,
            @RequestParam("reaction") String reaction) {
        System.out.println("postId: " + postId);
        System.out.println("reaction: " + reaction);
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        Reaction newReaction = new Reaction();
        newReaction.setReaction(reaction);
        newReaction.setPost(postDao.getReferenceById(postId));
        newReaction.setUser(user);
        reactionDao.save(newReaction);

        return "redirect:/home";
    }

    @PostMapping("/reaction/post/submit/{groupId}")
    public String submitGroupReaction(
            @RequestParam("postId") Long postId,
            @RequestParam("reaction") String reaction) {
        System.out.println("postId: " + postId);
        System.out.println("reaction: " + reaction);
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        Reaction newReaction = new Reaction();
        newReaction.setReaction(reaction);
        newReaction.setPost(postDao.getReferenceById(postId));
        newReaction.setUser(user);
        reactionDao.save(newReaction);

        return "redirect:/group/{groupId}";
    }

    @PostMapping("/reaction/comment/submit")
    public String submitCommentReaction(
            @RequestParam("commentId") Long commentId,
            @RequestParam("reaction") String reaction) {
        System.out.println("commentId: " + commentId);
        System.out.println("reaction: " + reaction);
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        Reaction newReaction = new Reaction();
        newReaction.setReaction(reaction);
        newReaction.setComment(commentDao.getReferenceById(commentId));
        newReaction.setUser(user);
        reactionDao.save(newReaction);

        return "redirect:/home";
    }

    @PostMapping("/reaction/comment/submit/{groupId}")
    public String submitGroupCommentReaction(
            @RequestParam("commentId") Long commentId,
            @RequestParam("reaction") String reaction) {
        System.out.println("commentId: " + commentId);
        System.out.println("reaction: " + reaction);
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        Reaction newReaction = new Reaction();
        newReaction.setReaction(reaction);
        newReaction.setComment(commentDao.getReferenceById(commentId));
        newReaction.setUser(user);
        reactionDao.save(newReaction);

        return "redirect:/group/{groupId}";
    }

}
