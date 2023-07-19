package com.geekguild.controllers;

import com.geekguild.models.Portfolio;
import com.geekguild.models.User;
import com.geekguild.models.Work;
import com.geekguild.repositories.PortfolioRepository;
import com.geekguild.repositories.UserRepository;
import com.geekguild.repositories.WorkRepository;
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

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        userDao.save(user);
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolioDao.save(portfolio);
        Work work = new Work();
        work.setUser(user);
        workDao.save(work);


        return "redirect:/login";
        //register page login button taking me to about us/homepage.
        //homepage -profile button -GROUPS button -add friend -
        //groups page - button to group(GROUPS W/ID)


    }


    @PostMapping("/profile/{id}/delete")
    public String deleteProfile(@PathVariable long id) {
        System.out.println(id);
        userDao.deleteById(id);
        return "redirect:/register";
    }

//    @DeleteMapping("/profile/{id}/delete")
//    String deleteUser(@PathVariable Long id) {
//        userDao.deleteById(id);
//        return "redirect:/users/register";
//    }



}