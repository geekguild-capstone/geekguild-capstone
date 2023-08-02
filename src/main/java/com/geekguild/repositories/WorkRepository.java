package com.geekguild.repositories;

import com.geekguild.models.Portfolio;
import com.geekguild.models.Post;
import com.geekguild.models.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("SELECT w FROM Work w WHERE w.user.id = :userId")
    Work findByUserId(Long userId);
}
