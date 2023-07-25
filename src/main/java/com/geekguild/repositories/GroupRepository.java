package com.geekguild.repositories;

import com.geekguild.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;



public interface GroupRepository extends JpaRepository<Group, Long> {
}
