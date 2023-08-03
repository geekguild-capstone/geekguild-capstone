package com.geekguild.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.geekguild.services.UserDetailsLoader;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

//    @Autowired
//    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;


    private UserDetailsLoader usersLoader;

    public SecurityConfiguration(UserDetailsLoader usersLoader) {
        this.usersLoader = usersLoader;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                /* Login configuration */
                .formLogin((login) -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true))

            /* Logout configuration */
                .logout((logout) -> logout.logoutSuccessUrl("/"))


                .authorizeHttpRequests((requests) -> requests
                        /* Pages that require authentication
                         * only authenticated users can create and edit ads */
                        .requestMatchers("/posts/create", "/posts/*/edit", "/profile/*/delete").authenticated()
                        /* Pages that do not require authentication
                         * anyone can visit the home page, register, login, and view ads */

                        .requestMatchers("/", "/posts", "/posts/*", "/register", "/login", "/home", "/groups", "/group", "/group/*", "/about-us", "/profile", "/profile/*/edit", "/profile/upload/banner", "/profile/upload/image", "/profile/*", "/home/upload", "/friends", "/friends/*/accept", "/friends/*/reject", "/friends/add",
                                "/friends/remove","/group/create", "/group/*/join", "/group/*/leave", "/group/*/comment", "/group/*/addComment/*", "/post/create", "/post/delete/*", "/post/delete/*/*","/post/*/create", "/comment", "/comment/delete/*", "/comment/delete/*/*", "/post/*/update",  "/post/*", "/register/check-email", "/reaction/post/submit", "/reaction/post/submit/*", "/reaction/comment/submit", "/reaction/comment/submit/*", "/comment/*", "/comment/*/update").permitAll()

                        // allow loading of static resources
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

}

