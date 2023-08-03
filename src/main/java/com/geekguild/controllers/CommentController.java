package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import jakarta.transaction.Transactional;
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

    //Delete comment to a specific post
    @Transactional
    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable long commentId) {
        // Get the currently logged-in user
        User loggedInUser = getCurrentLoggedInUser();


        // Find the comment by ID
        Comments comment = commentDao.getReferenceById(commentId);


        // Check if the logged-in user is the owner of the post
        if (comment.getUser().getId() != loggedInUser.getId()) {
            // User is not the owner, return an error or handle it as you prefer
            return "redirect:/error"; // For example, redirect to an error page
        }

        // Delete all the reactions associated with the commentId
        reactionDao.deleteReactionsByCommentId(commentId);



        // Use the custom query method to delete the post by ID
        commentDao.deleteCommentById(commentId);
        return "redirect:/home";
    }

    //Delete comment to a specific post
    @Transactional
    @PostMapping("/comment/delete/{commentId}/{groupId}")
    public String deleteGroupComment(@PathVariable long commentId) {
        // Get the currently logged-in user
        User loggedInUser = getCurrentLoggedInUser();


        // Find the comment by ID
        Comments comment = commentDao.getReferenceById(commentId);


        // Check if the logged-in user is the owner of the post
        if (comment.getUser().getId() != loggedInUser.getId()) {
            // User is not the owner, return an error or handle it as you prefer
            return "redirect:/error"; // For example, redirect to an error page
        }

        // Delete all the reactions associated with the commentId
        reactionDao.deleteReactionsByCommentId(commentId);



        // Use the custom query method to delete the post by ID
        commentDao.deleteCommentById(commentId);
        return "redirect:/group/{groupId}";
    }

    //Add comment to a specific post
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
