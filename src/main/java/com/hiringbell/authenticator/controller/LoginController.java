package com.hiringbell.authenticator.controller;

import com.hiringbell.authenticator.entity.UserEntity;
import com.hiringbell.authenticator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/registration")
public class LoginController {
    @Autowired
    private UserRepository userRepository; // Replace with your user repository

    @PostMapping("/google")
    public UserEntity registerWithGoogle(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name"); // Extract desired fields

        // Check if user already exists with this email
        UserEntity result = userRepository.findByEmail(email);
        if (result == null) {
            saveNewUser(email);
        }

        return result;
    }

    private void saveNewUser(String email) {
        // Create a new user object with required fields
        UserEntity user = new UserEntity();
        user.setEmail(email);
        userRepository.save(user);
    }
}