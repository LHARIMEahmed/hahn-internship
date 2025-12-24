package com.example.hahn_internship.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.hahn_internship.model.User;
import com.example.hahn_internship.repository.UserRepository;
import com.example.hahn_internship.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that handles JWT authentication for incoming HTTP requests.
 * This filter extracts the JWT token from the Authorization header,
 * validates it, and sets the authentication in the SecurityContext.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Intercepts HTTP requests and performs JWT validation.
     * If a valid JWT is found, sets the authenticated user in the SecurityContext.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException in case of a servlet error
     * @throws IOException in case of an I/O error
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
                                    throws ServletException, IOException {

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check if header contains Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);

            // Validate token and set authentication
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null && jwtService.isTokenValid(token, user)) {

                    // Build UserDetails from user entity
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities(List.of()) // Add roles here if you have any
                            .build();

                    // Create authentication token and set it in the SecurityContext
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
