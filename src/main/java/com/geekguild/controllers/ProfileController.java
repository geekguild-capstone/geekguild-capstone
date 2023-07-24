package com.geekguild.controllers;

import com.geekguild.models.*;
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
import java.util.Optional;

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

    @GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable("id") Long userId, Model model) {
        // Retrieve the user with the given userId from the database
        User user = userDao.findById(userId).orElse(null);

        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }

        // Fetch the user's portfolio and work
        Portfolio portfolio = portfolioDao.findByUserId(userId);
        Work work = workDao.findByUserId(userId);

        // Add the user, portfolio, and work to the model so they can be accessed in the view
        model.addAttribute("user", user);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("work", work);

        return "users/profile";
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
    public String editProfileLoad(@PathVariable long id, Model model) {
        Long userId = id;
        User user = userDao.getReferenceById(userId);
        Portfolio portfolio = portfolioDao.getReferenceById(id);
        Work work = workDao.getReferenceById(id);
        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }

        ProfileFormWrapper profileFormWrapper = new ProfileFormWrapper();
        profileFormWrapper.setUser(user);
        profileFormWrapper.setPortfolio(portfolio);
        profileFormWrapper.setWork(work);

        model.addAttribute("profileFormWrapper", profileFormWrapper);

        return "users/edit";
    }


    @PostMapping("/profile/{id}/edit")
    public String editProfile(@PathVariable long id, @ModelAttribute("profileFormWrapper") ProfileFormWrapper profileFormWrapper) {
        // Fetch the existing user from the database
        User loggedInUser = userDao.findById(id).orElse(null);
        if (loggedInUser == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error";
        }

        // Update the user fields if they are not null
        User formUser = profileFormWrapper.getUser();
        if (formUser.getUsername() != null) {
            loggedInUser.setUsername(formUser.getUsername());
        }
        if (formUser.getFirstname() != null) {
            loggedInUser.setFirstname(formUser.getFirstname());
        }
        if (formUser.getLastname() != null) {
            loggedInUser.setLastname(formUser.getLastname());
        }
        if (formUser.getEmail() != null) {
            loggedInUser.setEmail(formUser.getEmail());
        }
        // Add more fields as needed...

        // Save the updated user back to the database
        userDao.save(loggedInUser);

        // Fetch the existing portfolio and update its fields
        Portfolio existingPortfolio = portfolioDao.findByUserId(id);
        if (existingPortfolio != null) {
            Portfolio formPortfolio = profileFormWrapper.getPortfolio();
            if (formPortfolio.getAbout() != null) {
                existingPortfolio.setAbout(formPortfolio.getAbout());
            }
            if (formPortfolio.getProj1() != null) {
                existingPortfolio.setProj1(formPortfolio.getProj1());
            }
            if (formPortfolio.getProj2() != null) {
                existingPortfolio.setProj2(formPortfolio.getProj2());
            }
            if (formPortfolio.getProj3() != null) {
                existingPortfolio.setProj3(formPortfolio.getProj3());
            }
            if (formPortfolio.getLinkedin() != null) {
                existingPortfolio.setLinkedin(formPortfolio.getLinkedin());
            }
            if (formPortfolio.getFacebook() != null) {
                existingPortfolio.setFacebook(formPortfolio.getFacebook());
            }
            if (formPortfolio.getGithub() != null) {
                existingPortfolio.setGithub(formPortfolio.getGithub());
            }
            if (formPortfolio.getMisclink() != null) {
                existingPortfolio.setMisclink(formPortfolio.getMisclink());
            }
            if (formPortfolio.getHeadline() != null) {
                existingPortfolio.setHeadline(formPortfolio.getHeadline());
            }
            // Add more fields as needed...
            // Save the updated portfolio back to the database
            portfolioDao.save(existingPortfolio);
        }

        // Fetch the existing work and update its fields
        Work existingWork = workDao.findByUserId(id);
        if (existingWork != null) {
            Work formWork = profileFormWrapper.getWork();
            if (formWork.getAsk() != null) {
                existingWork.setAsk(formWork.getAsk());
            }
            if (formWork.getLearn() != null) {
                existingWork.setLearn(formWork.getLearn());
            }
            if (formWork.getFact() != null) {
                existingWork.setFact(formWork.getFact());
            }
            if (formWork.getHelp() != null) {
                existingWork.setHelp(formWork.getHelp());
            }
            if (formWork.getWorking() != null) {
                existingWork.setWorking(formWork.getWorking());
            }
            // Add more fields as needed...
            // Save the updated work back to the database
            workDao.save(existingWork);
        }

        return "redirect:/profile";
    }



}
