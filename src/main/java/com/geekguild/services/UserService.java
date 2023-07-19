//package com.geekguild.services;
//
//import com.geekguild.models.Portfolio;
//import com.geekguild.models.User;
//import com.geekguild.repositories.UserRepository;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public void createUser(User user) {
//        userRepository.save(user);
//
//        Portfolio portfolio = new Portfolio();
//        portfolio.setUser(user);
//        // Set profile details here
//
//        userRepository.save(user);
//    }
//}