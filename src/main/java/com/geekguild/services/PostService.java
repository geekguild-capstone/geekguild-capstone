package com.geekguild.services;

import com.geekguild.models.Post;
import com.geekguild.models.Reaction;
import com.geekguild.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Reaction> getReactionsForPost(long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return post.getReactions();
        }
        return null;
    }
}
