package com.geekguild.repositories;

import com.geekguild.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Post, Long> {


}

