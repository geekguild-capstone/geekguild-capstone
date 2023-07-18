package com.geekguild.repositories;

import com.geekguild.models.Post;
import com.geekguild.models.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, Long> {
}
