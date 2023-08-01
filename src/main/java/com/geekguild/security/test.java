//package com.geekguild.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//public class test {
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//
//        http
//                /* Login configuration */
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/home", true) // user's home page, it can be any URL
//                .permitAll() // Anyone can go to the login page
//
//                /* Logout configuration */
//                .and()
//                .logout()
//                .logoutSuccessUrl("/") // append a query string value
//
//                .and()
//                .authorizeHttpRequests((requests) -> requests
//                        /* Pages that require authentication
//                         * only authenticated users can create and edit ads */
//                        .requestMatchers("/posts/create", "/posts/*/edit", "/profile/*/delete").authenticated()
//                        /* Pages that do not require authentication
//                         * anyone can visit the home page, register, login, and view ads */
//
//                        .requestMatchers("/", "/posts", "/posts/*", "/register", "/login", "/home", "/groups", "/group", "/group/*", "/about-us", "/profile", "/profile/*/edit", "/profile/upload", "/profile/*", "/home/upload", "/friends", "/friends/*/accept", "/friends/*/reject", "/friends/add",
//                                "/friends/remove","/group/create", "/group/*/join", "/group/*/leave", "/group/*/comment", "/post/create", "/post/delete/*", "/post/*/create", "/comment", "/comment/delete/*", "/post/*/update", "/post/*", "/register/check-email", "/reaction/submit").permitAll()
//
//                        // allow loading of static resources
//                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }
//
//
//
//
//}
