//package com.geekguild.controllers;
//
//import com.geekguild.models.Post;
//import com.geekguild.models.Reaction;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ReactionController {
//
//    // ...
//
//    public void addPostReaction(Long postId, Reaction reaction) {
//        Post post = postRepository.findById(postId).orElse(null);
//        if (post == null) {
//            throw new EntityNotFoundException("Post not found");
//        }
//
//        User user = userService.getUserById(reaction.getUser().getId());
//        if (user == null) {
//            throw new EntityNotFoundException("User not found");
//        }
//
//        // Add the user and post to the reaction
//        reaction.getUsers().add(user);
//        reaction.getPosts().add(post);
//
//        // Save the reaction
//        reactionRepository.save(reaction);
//    }
//
//    public void addCommentReaction(Long commentId, Reaction reaction) {
//        Comment comment = commentRepository.findById(commentId).orElse(null);
//        if (comment == null) {
//            throw new EntityNotFoundException("Comment not found");
//        }
//
//        User user = userService.getUserById(reaction.getUser().getId());
//        if (user == null) {
//            throw new EntityNotFoundException("User not found");
//        }
//
//        // Add the user and comment to the reaction
//        reaction.getUsers().add(user);
//        reaction.getComments().add(comment);
//
//        // Save the reaction
//        reactionRepository.save(reaction);
//    }
//
//    // ...
//}
