package com.example.hahn_internship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.hahn_internship.dto.AuthResponse;
import com.example.hahn_internship.dto.LoginRequest;
import com.example.hahn_internship.dto.RegisterRequest;
import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    /**
     * Login user
     * @param request
     * @return
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(!passwordEncoder.matches(request.password(),user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        } else {
            String token = jwtService.generateToken(user);
            return new AuthResponse(token, user.getId(), user.getEmail());
        }
    }
    /**
     * Register user
     * @param request
     * @return
     */
    public AuthResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent()){
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
