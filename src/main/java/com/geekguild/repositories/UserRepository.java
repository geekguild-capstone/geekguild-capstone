package com.geekguild.repositories;

import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    default User getReferenceById(Long userId) {

        return findById(userId).orElse(null);
    }

}


