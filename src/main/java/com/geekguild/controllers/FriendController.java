package com.geekguild.controllers;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendController(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    // Endpoint to get all friend requests for a specific user
    @GetMapping("/{userId}/friend-requests")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(user, "pending");
            return ResponseEntity.ok(friendRequests);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to accept a friend request
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);
        if (optionalFriendRequest.isPresent()) {
            FriendRequest friendRequest = optionalFriendRequest.get();
            friendRequest.setStatus("accepted");
            friendRequestRepository.save(friendRequest);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to reject a friend request
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);
        if (optionalFriendRequest.isPresent()) {
            FriendRequest friendRequest = optionalFriendRequest.get();
            friendRequest.setStatus("rejected");
            friendRequestRepository.save(friendRequest);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
