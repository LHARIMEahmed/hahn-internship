package com.example.hahn_internship.dto;

public record AuthResponse(
    String token,
    Long userId,
    String email
) {

}
