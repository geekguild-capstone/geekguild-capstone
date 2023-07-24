package com.geekguild.controllers;

import com.geekguild.models.Portfolio;
import com.geekguild.models.User;
import com.geekguild.models.Work;
import com.geekguild.repositories.PortfolioRepository;
import com.geekguild.repositories.UserRepository;
import com.geekguild.repositories.WorkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;

@Controller
public class ProfileController {

    private PortfolioRepository portfolioDao;
    private UserRepository userDao;
    private WorkRepository workDao;

    public ProfileController(UserRepository userDao, PortfolioRepository portfolioDao, WorkRepository workDao) {
        this.portfolioDao = portfolioDao;
        this.userDao = userDao;
        this.workDao = workDao;
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
        Portfolio portfolio = portfolioDao.findByUserId(loggedInUser.getId());
        model.addAttribute("portfolio", portfolio);
        Work work = workDao.findByUserId(loggedInUser.getId());
        model.addAttribute("work", work);

        return "users/profile";
    }


    @GetMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, Model model) {
        Long userId = id;
        User user = userDao.getReferenceById(userId);
        Portfolio portfolio = portfolioDao.getReferenceById(id);
        Work work = workDao.getReferenceById(id);
        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }
        model.addAttribute("user", user);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("work", work);
        return "/users/edit";
    }

    // This method handles the POST request to save the edited profile
    @PostMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, @ModelAttribute User user, @ModelAttribute Portfolio portfolio, @ModelAttribute Work work) {
        User loggedInUser = userDao.getReferenceById(id);
        loggedInUser.setUsername(user.getFirstname());
        loggedInUser.setEmail(user.getLastname());
        loggedInUser.setEmail(user.getEmail());
        userDao.save(loggedInUser);

        // Fetch the existing portfolio and update its fields
        Portfolio existingPortfolio = portfolioDao.findByUserId(id);
        existingPortfolio.setAbout(portfolio.getAbout());

        // Save the updated portfolio back to the database
        portfolioDao.save(existingPortfolio);

        // Fetch the existing work and update its fields
        Work existingWork = workDao.findByUserId(id);
        existingWork.setAsk(work.getAsk());

        // Save the updated work back to the database
        workDao.save(existingWork);

        return "redirect:/profile/" + id;
    }

    @GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable("id") Long userId, Model model) {
        // Retrieve the user with the given userId from the database
        User user = userDao.getReferenceById(userId);


        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }

        Portfolio portfolio = portfolioDao.findByUserId(user.getId());
        model.addAttribute("portfolio", portfolio);

        // Add the user to the model so it can be accessed in the view
        model.addAttribute("user", user);
        Work work = workDao.findByUserId(user.getId());
        model.addAttribute("work", work);

        return "users/profile";
    }



}
