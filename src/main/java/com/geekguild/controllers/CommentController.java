package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class CommentController {

    private final PostRepository postDao;
    private final GroupRepository groupDao;
    private final CommentRepository commentDao;
    private final ReactionRepository reactionDao;

    private final UserRepository userDao;

    public CommentController(PostRepository postDao, GroupRepository groupDao, CommentRepository commentDao, ReactionRepository reactionDao, UserRepository userDao) {
        this.postDao = postDao;
        this.groupDao = groupDao;
        this.commentDao = commentDao;
        this.reactionDao = reactionDao;
        this.userDao = userDao;
    }
    // Update a post
    @PostMapping("/comment/{commentId}/update")
    @ResponseBody
    public ResponseEntity<String> updatePost(@PathVariable long commentId, @RequestBody CommentUpdateRequest request) {
        Comments comment = commentDao.findById(commentId).orElse(null);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the logged-in user is the owner of the post or has permission to edit it
        User loggedInUser = getCurrentLoggedInUser();
        if (loggedInUser.getId() != comment.getUser().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to edit this post.");
        }

        // Update the post with the new data
        comment.setText(request.getText());
        comment.setSnippet(request.getSnippet());
        commentDao.save(comment);

        return ResponseEntity.ok("Post updated successfully.");
    }

@GetMapping("/comment/{commentId}")
@ResponseBody
public ResponseEntity<CommentsDto> getCommentById(@PathVariable long commentId) {
    Comments comment = commentDao.findById(commentId).orElse(null);
    if (comment == null) {
        return ResponseEntity.notFound().build();
    }

    // Convert the Comments entity to the CommentsDto
    CommentsDto commentDto = new CommentsDto(
            comment.getId(),
            comment.getText(),
            comment.getSnippet()
            // Set other fields if needed
    );

    return ResponseEntity.ok(commentDto);
}


    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
