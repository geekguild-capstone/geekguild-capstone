package com.geekguild.controllers;

import com.geekguild.models.*;
import com.geekguild.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    private PortfolioRepository portfolioDao;
    private UserRepository userDao;
    private WorkRepository workDao;
    private LanguageRepository languageDao;

    private GroupRepository groupDao;

    public ProfileController(UserRepository userDao, PortfolioRepository portfolioDao, WorkRepository workDao, LanguageRepository languageDao, GroupRepository groupDao) {
        this.portfolioDao = portfolioDao;
        this.userDao = userDao;
        this.workDao = workDao;
        this.languageDao = languageDao;
        this.groupDao = groupDao;
    }

    @GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable("id") Long userId, Model model) {
        User loggedInUser = getCurrentLoggedInUser();

        //Get logged in users groups for the navbar
        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        model.addAttribute("listGroups", loggedInUserGroups);


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


    @PostMapping("/profile/upload/image")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestParam("fileURL") String fileURL) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        user.setImage(fileURL);

        userDao.save(user);

        return ResponseEntity.ok("Image URL saved successfully.");
    }

    @PostMapping("/profile/upload/banner")
    @ResponseBody
    public ResponseEntity<String> uploadBanner(@RequestParam("fileURLBanner") String fileURLBanner) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.getReferenceById(loggedInUser.getId());
        user.setBanner(fileURLBanner);

        userDao.save(user);

        return ResponseEntity.ok("Banner URL saved successfully.");
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Get logged in users groups for the navbar
        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        model.addAttribute("listGroups", loggedInUserGroups);

        User user = userDao.getReferenceById(loggedInUser.getId());
        model.addAttribute("user", user);
        Portfolio portfolio = portfolioDao.findByUserId(loggedInUser.getId());
        model.addAttribute("portfolio", portfolio);
        Work work = workDao.findByUserId(loggedInUser.getId());
        model.addAttribute("work", work);
        List<Language> userLanguages = user.getLanguages();
        model.addAttribute("userLanguages", userLanguages);

        //add dynamic page title upon load
        model.addAttribute("title", "GeekGuild - your profile");

        return "users/profile";
    }


    @GetMapping("/profile/{userId}/edit")
    public String editProfileLoad(@PathVariable long userId, Model model) {
        User loggedInUser = getLoggedInUser();
        //Get logged in users groups for the navbar
        List<Group> loggedInUserGroups = groupDao.findByMembersContaining(loggedInUser);
        model.addAttribute("listGroups", loggedInUserGroups);

        User user = userDao.getReferenceById(loggedInUser.getId());
        Portfolio portfolio = portfolioDao.findByUserId(userId);
        Work work = workDao.findByUserId(userId);
        List<Language> allLanguages = languageDao.findAll();

        // Check if the user exists
        if (user == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error"; // Example: redirect to an error page
        }

        ProfileFormWrapper profileFormWrapper = new ProfileFormWrapper();
        profileFormWrapper.setUser(user);
        profileFormWrapper.setPortfolio(portfolio);
        profileFormWrapper.setWork(work);

        model.addAttribute("allLanguages", allLanguages);
        model.addAttribute("user", user);
        model.addAttribute("profileFormWrapper", profileFormWrapper);

        return "users/edit";
    }


    @PostMapping("/profile/{userId}/edit")
    public String editProfile(@PathVariable long userId, @ModelAttribute("profileFormWrapper") ProfileFormWrapper profileFormWrapper, @RequestParam(value = "selectedLanguages", required = false) List<Long> selectedLanguageIds, Model model, @RequestParam("fileURLBanner") String banner) {
        System.out.println(banner);
        // Fetch the existing user from the database
        User loggedIn = getCurrentLoggedInUser();
        User loggedInUser = userDao.getReferenceById(loggedIn.getId());
        if (loggedInUser == null) {
            // Handle the case when the user doesn't exist (you can show an error page or redirect to a different page)
            return "redirect:/error";
        }

//        User loggedInUser = getCurrentLoggedInUser();

        loggedInUser.setBanner(banner);

        // Update the user fields if they are not null
        User formUser = profileFormWrapper.getUser();
        if (formUser.getFirstname() != null) {
            loggedInUser.setFirstname(formUser.getFirstname());
        }
        if (formUser.getLastname() != null) {
            loggedInUser.setLastname(formUser.getLastname());
        }
        if (formUser.getEmail() != null) {
            loggedInUser.setEmail(formUser.getEmail());
        }

        // Save the updated user back to the database
        userDao.save(loggedInUser);

        // Fetch the existing user languages
        List<Language> userLanguages = loggedInUser.getLanguages();

        // Update the user's languages based on the selectedLanguageIds
        if (selectedLanguageIds != null) {
            List<Language> selectedLanguages = languageDao.findAllById(selectedLanguageIds);
            // Add the selected languages to the user's languages if they are not already present
            for (Language language : selectedLanguages) {
                if (!userLanguages.contains(language)) {
                    userLanguages.add(language);
                }
            }
            // Save the updated user languages back to the database
            loggedInUser.setLanguages(userLanguages);
            userDao.save(loggedInUser);
        }

        // Handle the unchecked languages
        // Get the list of language IDs that are currently selected for the user
        List<Long> currentSelectedLanguageIds = userLanguages != null
                ? userLanguages.stream().map(Language::getId).collect(Collectors.toList())
                : new ArrayList<>();
        // Find the language IDs that are unchecked (present in currentSelectedLanguageIds but not in selectedLanguageIds)
        List<Long> uncheckedLanguageIds;
        if (selectedLanguageIds != null) {
            uncheckedLanguageIds = currentSelectedLanguageIds.stream()
                    .filter(languageId -> !selectedLanguageIds.contains(languageId))
                    .collect(Collectors.toList());
        } else {
            uncheckedLanguageIds = new ArrayList<>();
        }

// Remove the unchecked languages from the user's languages and save the updated user languages back to the database
        if (!uncheckedLanguageIds.isEmpty()) {
            // Create a new list to hold the updated user languages
            List<Language> updatedUserLanguages = new ArrayList<>(userLanguages);

            // Remove the unchecked languages from the updatedUserLanguages list by matching their IDs
            updatedUserLanguages.removeIf(language -> uncheckedLanguageIds.contains(language.getId()));

            // Save the updated user languages back to the user object
            loggedInUser.setLanguages(updatedUserLanguages);

            // Save the updated user object back to the database
            userDao.save(loggedInUser);
        } else if (selectedLanguageIds != null) {
            // If selectedLanguageIds is not null but uncheckedLanguageIds is empty, it means all checkboxes are checked,
            // and we need to save the updated user languages back to the user object and database
            loggedInUser.setLanguages(userLanguages);
            userDao.save(loggedInUser);
        } else {
            // If selectedLanguageIds is null, it means no checkboxes are checked,
            // so we need to remove all languages from the user's languages and save the empty list back to the database
            userLanguages.clear();
            userDao.save(loggedInUser);
        }

        // Fetch the existing portfolio and update its fields
        Portfolio existingPortfolio = portfolioDao.findByUserId(userId);
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
        Work existingWork = workDao.findByUserId(userId);
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

            // Save the updated work back to the database
            workDao.save(existingWork);
        }

        return "redirect:/profile";
    }

    private User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private User getCurrentLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
