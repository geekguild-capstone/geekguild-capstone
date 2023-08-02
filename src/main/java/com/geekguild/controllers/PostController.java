package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class PostController {

    private final PostRepository postDao;
    private final GroupRepository groupDao;
    private final CommentRepository commentDao;
    private final ReactionRepository reactionDao;

    public PostController(PostRepository postDao, GroupRepository groupDao, CommentRepository commentDao, ReactionRepository reactionDao) {
        this.postDao = postDao;
        this.groupDao = groupDao;
        this.commentDao = commentDao;
        this.reactionDao = reactionDao;
    }

    //Creating a new post
    @PostMapping("/post/create")
    public String showCreatePostForm(@ModelAttribute Post post, @RequestParam("image") String image) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setUser(loggedInUser);
        post.setImage(image);

        // Check if the snippet field is empty (empty string) and set it to null
        if (post.getSnippet() != null && post.getSnippet().trim().isEmpty()) {
            post.setSnippet(null);
        }
        postDao.save(post);
        return "redirect:/home";
    }

    // Delete a Post
    @Transactional
    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable long postId) {
        // Get the currently logged-in user
        User loggedInUser = getCurrentLoggedInUser();


        // Find the post by ID
        Post post = postDao.findById(postId).orElse(null);

        if (post == null) {
            // Handle post not found
            return "redirect:/error";
        }

        // Delete associated comments first
        List<Comments> comments = post.getComments();
        post.setComments(Collections.emptyList());
        postDao.save(post); // Save the post without comments

        for (Comments comment : comments) {
            // Delete each comment
            // Assuming you have a commentDao/repository to interact with comments
            commentDao.deleteById(comment.getId());
        }

        // Delete associated reactions
        reactionDao.deleteReactionsByPostId(postId);


        // Check if the logged-in user is the owner of the post
        if (post.getUser().getId() != loggedInUser.getId()) {
            // User is not the owner, return an error or handle it as you prefer
            return "redirect:/error"; // For example, redirect to an error page
        }

        // Use the custom query method to delete the post by ID
        postDao.deletePostById(postId);
        return "redirect:/home";
    }


    //Creating a post within a group "This may be able to be combined with normal create post"
    @PostMapping("/post/{groupId}/create")
    public String createPost(@PathVariable long groupId, @ModelAttribute Post post, @RequestParam("image") String image) {
        User loggedInUser = getCurrentLoggedInUser();
        post.setUser(loggedInUser);

        Group group = groupDao.findById(groupId).orElse(null);
        if (group == null) {
            // Handle invalid group ID
            return "redirect:/error";
        }

        post.setGroup(group);
        post.setImage(image);

        postDao.save(post);
        return "redirect:/group/{groupId}";


    }

    // Update a post
    @PostMapping("/post/{postId}/update")
    @ResponseBody
    public ResponseEntity<String> updatePost(@PathVariable long postId, @RequestBody PostUpdateRequest request) {
        Post post = postDao.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the logged-in user is the owner of the post or has permission to edit it
        User loggedInUser = getCurrentLoggedInUser();
        if (loggedInUser.getId() != post.getUser().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to edit this post.");
        }

        // Update the post with the new data
        post.setBody(request.getBody());
        post.setSnippet(request.getSnippet());
        postDao.save(post);

        return ResponseEntity.ok("Post updated successfully.");
    }


    @GetMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<PostDto> getPostById(@PathVariable long postId) {
        Post post = postDao.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        // Convert the Post entity to the PostDto
        PostDto postDto = new PostDto(
                post.getId(),
                post.getImage(),
                post.getBody(),
                post.getSnippet()
                // Set other fields if needed
        );

        return ResponseEntity.ok(postDto);
    }

    //COMMENTS

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

    //Edit comment to a specific post


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



    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
