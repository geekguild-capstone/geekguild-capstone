package com.geekguild.repositories;

import com.geekguild.models.Comments;
import com.geekguild.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
