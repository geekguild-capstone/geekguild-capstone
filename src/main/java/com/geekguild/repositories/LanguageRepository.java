package com.geekguild.repositories;

import com.geekguild.models.Language;
import com.geekguild.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {


}

