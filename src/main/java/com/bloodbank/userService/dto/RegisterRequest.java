package com.bloodbank.userService.dto;


import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    private String bloodGroup;
    private String contact;
    private String address;

    private String role; // for roles [DONOR, RECIPIENT, ADMIN]
}
