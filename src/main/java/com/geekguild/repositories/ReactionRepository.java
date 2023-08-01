package com.geekguild.repositories;

import com.geekguild.models.Group;
import com.geekguild.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
