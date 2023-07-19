//package com.geekguild.services;
//
//import com.geekguild.models.Portfolio;
//import com.geekguild.models.User;
//import com.geekguild.repositories.PortfolioRepository;
//import com.geekguild.repositories.UserRepository;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//        private final UserRepository userRepository;
//        private final PortfolioRepository portfolioRepository;
//
//        public UserService(UserRepository userRepository, PortfolioRepository portfolioRepository) {
//            this.userRepository = userRepository;
//            this.portfolioRepository = portfolioRepository;
//        }
//
//        public void createUser(User user) {
//            userRepository.save(user);
//
//            Portfolio portfolio = new Portfolio();
//            portfolio.setUser(user);
//            portfolioRepository.save(portfolio);
//        }
//    }