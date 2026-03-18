package com.bloodbank.userService.dto;

import com.bloodbank.userService.entity.Role;
import lombok.Data;

// Response returned after successful login (JWT token)
@Data
public class AuthResponse {
    private String token;   // JWT token
    private String message; // success message
    private String role;
}
