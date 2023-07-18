package com.geekguild.controllers;

import com.geekguild.models.User;
import com.geekguild.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private UserRepository userDao;

    public ProfileController(UserRepository userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/profile/upload")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestParam("fileURL") String fileURL) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        user.setImage(fileURL);
        userDao.save(user);

        return ResponseEntity.ok("Image URL saved successfully.");
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);

        return "/users/profile";
    }

    @GetMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, Model model) {
        Long userId = id;
        User user = userDao.getReferenceById(userId);
        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }
        model.addAttribute("user", user);
        return "/users/edit";
    }


}
