package com.example.hahn_internship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hahn_internship.dto.AuthResponse;
import com.example.hahn_internship.dto.LoginRequest;
import com.example.hahn_internship.dto.RegisterRequest;
import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.UserRepository;

/**
 * Service responsible for user authentication and registration.
 * Handles login, registration, and JWT token generation.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Authenticates a user using email and password.
     * Generates a JWT token if authentication is successful.
     *
     * @param request the login request containing email and password
     * @return AuthResponse containing the JWT token, user ID, and email
     * @throws RuntimeException if user is not found or credentials are invalid
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        } else {
            String token = jwtService.generateToken(user);
            return new AuthResponse(token, user.getId(), user.getEmail());
        }
    }

    /**
     * Registers a new user and generates a JWT token.
     *
     * @param request the registration request containing user details
     * @return AuthResponse containing the JWT token, user ID, and email
     * @throws RuntimeException if the email is already in use
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());

        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser);
        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail());
    }
}
