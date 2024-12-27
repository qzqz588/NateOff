package com.example.nateoff.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
    private boolean rememberMe; // Remember Me 옵션

    // Getters and Setters
}