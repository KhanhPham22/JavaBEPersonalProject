package com.project.spring.personal.dto.Auth;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String role;
}
