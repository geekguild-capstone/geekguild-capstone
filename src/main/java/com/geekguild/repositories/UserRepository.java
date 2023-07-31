package com.geekguild.repositories;

import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id <> :loggedInUserId AND u NOT IN " +
            "(SELECT f.sender FROM FriendRequest f WHERE f.receiver.id = :loggedInUserId " +
            "AND f.status IN ('pending', 'accepted')) AND u NOT IN " +
            "(SELECT f.receiver FROM FriendRequest f WHERE f.sender.id = :loggedInUserId " +
            "AND f.status IN ('pending', 'accepted'))")
    List<User> findUsersNotFriendsWithAndNotPending(@Param("loggedInUserId") Long loggedInUserId);


    default User getReferenceById(Long userId) {
        return findById(userId).orElse(null);
    }

    boolean existsByEmail(String email);
}


