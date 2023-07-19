package com.geekguild.repositories;

import com.geekguild.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{



}
