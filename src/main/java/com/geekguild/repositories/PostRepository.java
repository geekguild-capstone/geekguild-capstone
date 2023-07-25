package com.geekguild.repositories;

import com.geekguild.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByGroupId(Long groupId);




}
