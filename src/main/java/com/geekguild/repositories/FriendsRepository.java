package com.geekguild.repositories;

import com.geekguild.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
// Custom query methods, if needed

}
