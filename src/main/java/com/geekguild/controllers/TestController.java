package com.geekguild.controllers;

import com.geekguild.models.User;
import com.geekguild.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class TestController {

    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/friends/{userId}/friend-requests-view")
//    public String friendRequestsView(@PathVariable Long userId, Model model) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            model.addAttribute("user", user);
//            return "partials/friendRequest"; // Assuming that your HTML file for displaying friend requests is named "friendRequests.html" and located in the "resources" folder.
//        } else {
//            return "partials/userNotFound"; // Assuming that you have an "userNotFound.html" view to display if the user with the specified ID is not found.
//        }
//    }

    @GetMapping("/friends")
    public String index() {
        return "partials/friendRequest"; // Assuming that your HTML file is named "index.html" and located in the "resources" folder.
    }
}
