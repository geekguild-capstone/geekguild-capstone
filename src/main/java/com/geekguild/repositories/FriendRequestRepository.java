package com.geekguild.repositories;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
// Custom query methods, if needed
List<FriendRequest> findByReceiverAndStatus(User receiver, String status);
List<FriendRequest> findBySenderAndStatus(User sender, String status);


}
