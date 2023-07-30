package com.geekguild.repositories;

import com.geekguild.models.Post;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByGroupId(Long groupId);

    List<Post> findByGroupIdIsNull();

    List<Post> findByGroupIdAndUser(Long groupId, User user);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :postId")
    void deletePostById(Long postId);
}
