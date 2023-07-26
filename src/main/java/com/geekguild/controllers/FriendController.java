package com.geekguild.controllers;

import com.geekguild.models.FriendRequest;
import com.geekguild.models.User;
import com.geekguild.repositories.FriendRequestRepository;
import com.geekguild.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/friends")
public class FriendController {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendController(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String getFriendRequests(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FriendRequest> friendRequests = friendRequestRepository.findByReceiverAndStatus(loggedInUser, "pending");
        System.out.println(friendRequests);
        model.addAttribute("requests", friendRequests);
        return "partials/friendRequest";
    }

    @PostMapping("/{requestId}/accept")
    public String acceptFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);
        optionalFriendRequest.ifPresent(friendRequest -> {
            friendRequest.setStatus("accepted");
            friendRequestRepository.save(friendRequest);
        });
        return "redirect:/friends";
    }

    @PostMapping("/{requestId}/reject")
    public String rejectFriendRequest(@PathVariable Long requestId) {
        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(requestId);
        optionalFriendRequest.ifPresent(friendRequest -> {
            friendRequest.setStatus("rejected");
            friendRequestRepository.save(friendRequest);
        });
        return "redirect:/friends";
    }
}

