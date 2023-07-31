package com.geekguild.controllers;

import com.geekguild.models.Portfolio;
import com.geekguild.models.User;
import com.geekguild.models.Work;
import com.geekguild.repositories.PortfolioRepository;
import com.geekguild.repositories.UserRepository;
import com.geekguild.repositories.WorkRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {


    private UserRepository userDao;
    private PortfolioRepository portfolioDao;
    private WorkRepository workDao;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, PortfolioRepository portfolioDao, WorkRepository workDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.portfolioDao = portfolioDao;
        this.workDao = workDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    // Your existing POST method, but return type changed to EmailCheckResponse
//    @PostMapping("/register/check-email")
//    public EmailCheckResponse checkEmail(@RequestBody EmailCheckRequest request) {
//        String email = request.getEmail();
//        boolean emailExists = checkIfEmailExistsInDatabase(email);
//
//        EmailCheckResponse response = new EmailCheckResponse();
//        response.setExists(emailExists);
//
//        return response;
//    }

//    @PostMapping("/register")
//    public String saveUser(@ModelAttribute User user) {
//        String hash = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hash);
//        userDao.save(user);
//        Portfolio portfolio = new Portfolio();
//        portfolio.setUser(user);
//        portfolioDao.save(portfolio);
//        Work work = new Work();
//        work.setUser(user);
//        workDao.save(work);
//
//
//        return "redirect:/login";
//        //register page login button taking me to about us/homepage.
//        //homepage -profile button -GROUPS button -add friend -
//        //groups page - button to group(GROUPS W/ID)
//
//
//    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, Model model) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        // Check for empty fields
//        if (user.getFirstname().trim().isEmpty()) {
//            model.addAttribute("firstNameError", "First name is required.");
//            return "users/register";
//        }
//
//        if (user.getLastname().trim().isEmpty()) {
//            model.addAttribute("lastNameError", "Last name is required.");
//            return "users/register";
//        }

//        if (user.getPassword().trim().isEmpty()) {
//            model.addAttribute("passwordError", "Password is required.");
//            return "users/register";
//        }

        try {
            userDao.save(user);
            Portfolio portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolioDao.save(portfolio);
            Work work = new Work();
            work.setUser(user);
            workDao.save(work);
            return "redirect:/login";
        } catch (DataIntegrityViolationException e) {
            // Unique constraint violation occurred (email already exists)
            model.addAttribute("emailError", "Email already used, please try another.");
            return "users/register";
        }
    }



    @PostMapping("/profile/{id}/delete")
    public String deleteProfile(@PathVariable long id) {
        System.out.println(id);
        userDao.deleteById(id);
        return "redirect:/register";
    }

    // Implement your logic to check if the email exists in the database
    private boolean checkIfEmailExistsInDatabase(String email) {
        // Your implementation to check if the email exists in the database
        // Return true if the email exists, otherwise false
        // For example:
        return userDao.existsByEmail(email);
    }


}

//    @DeleteMapping("/profile/{id}/delete")
//    String deleteUser(@PathVariable Long id) {
//        userDao.deleteById(id);
//        return "redirect:/users/register";
//    }



