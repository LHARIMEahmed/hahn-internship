package com.example.hahn_internship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hahn_internship.dto.AuthResponse;
import com.example.hahn_internship.dto.LoginRequest;
import com.example.hahn_internship.dto.RegisterRequest;
import com.example.hahn_internship.service.AuthService;

/**
 * Controller responsible for authentication-related endpoints.
 * Provides REST endpoints for user login and registration.
 * Allows cross-origin requests from the frontend running on localhost:3000.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    /**
     * Service that handles authentication logic.
     */
    @Autowired
    private AuthService authService;

    /**
     * Handles user login requests.
     * 
     * @param request the login request containing user credentials
     * @return an AuthResponse containing JWT token and user info
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Handles user registration requests.
     * 
     * @param request the registration request containing new user details
     * @return an AuthResponse containing JWT token and user info
     */
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
