//package com.geekguild.controllers;
//
//import com.geekguild.models.FriendRequest;
//import com.geekguild.models.User;
//import com.geekguild.repositories.FriendRequestRepository;
//import com.geekguild.repositories.UserRepository;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//public class FriendController {
//
//    private final UserRepository userDao;
//    private final FriendRequestRepository friendDao;
//
//    public FriendController(UserRepository userDao, FriendRequestRepository friendDao) {
//        this.userDao = userDao;
//        this.friendDao = friendDao;
//    }
//
////    @GetMapping("")
////    public String getFriendRequests(Model model) {
////        User loggedInUser = getLoggedInUser();
////        List<FriendRequest> friendRequests = friendDao.findByReceiverAndStatus(loggedInUser, "pending");
////        model.addAttribute("requests", friendRequests);
////        return "partials/friendRequest";
////    }
//
////    @PostMapping("/{requestId}/accept")
////    public String acceptFriendRequest(@PathVariable Long requestId) {
////        updateFriendRequestStatus(requestId, "accepted");
////        return "redirect:/home";
////    }
//
////    @PostMapping("/{requestId}/reject")
////    public String rejectFriendRequest(@PathVariable Long requestId) {
////        updateFriendRequestStatus(requestId, "rejected");
////        return "redirect:/home";
////    }
//
////    @PostMapping("/add")
////    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId) {
////        User loggedInUser = getLoggedInUser();
////        User receiver = userDao.findById(receiverId).orElse(null);
////
////        if (receiver != null) {
////            FriendRequest friendRequest = new FriendRequest();
////            friendRequest.setSender(loggedInUser);
////            friendRequest.setReceiver(receiver);
////            friendRequest.setStatus("pending");
////            friendDao.save(friendRequest);
////        }
////
////        return "redirect:/home";
////    }
////
////    @PostMapping("/remove")
////    public String removeFriend(@RequestParam("friendId") Long friendId, @RequestParam("loggedInUserType") String loggedInUserType) {
////        Long loggedInUserId = getUserIdFromSecurityContext();
////        User loggedInUser = userDao.findById(loggedInUserId).orElse(null);
////        User friend = userDao.findById(friendId).orElse(null);
////
////        if (loggedInUser != null && friend != null) {
////            FriendRequest friendRequest;
////            if ("sender".equals(loggedInUserType)) {
////                friendRequest = friendDao.findBySenderAndReceiverAndStatus(loggedInUser, friend, "accepted");
////            } else if ("receiver".equals(loggedInUserType)) {
////                friendRequest = friendDao.findBySenderAndReceiverAndStatus(friend, loggedInUser, "accepted");
////            } else {
////                return "redirect:/home";
////            }
////
////            if (friendRequest != null) {
////                friendRequest.setStatus("rejected");
////                friendDao.save(friendRequest);
////            }
////        }
////
////        return "redirect:/home";
////    }
//
////    private User getLoggedInUser() {
////        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////    }
//
////    private void updateFriendRequestStatus(Long requestId, String status) {
////        Optional<FriendRequest> optionalFriendRequest = friendDao.findById(requestId);
////        optionalFriendRequest.ifPresent(friendRequest -> {
////            friendRequest.setStatus(status);
////            friendDao.save(friendRequest);
////        });
////    }
//
//
////    private Long getUserIdFromSecurityContext() {
////        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        if (principal instanceof User) {
////            return ((User) principal).getId();
////        }
////        return null;
////    }
//
//    // Additional friend-related methods and endpoints can go here
//}
//
