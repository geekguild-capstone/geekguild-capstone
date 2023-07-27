package com.geekguild.repositories;

import com.geekguild.models.Post;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Post, Long> {


}


