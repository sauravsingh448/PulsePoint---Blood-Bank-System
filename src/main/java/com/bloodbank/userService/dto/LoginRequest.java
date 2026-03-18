package com.bloodbank.userService.dto;

import lombok.Data;

// loginRequest used to receive the login data from the user.
@Data
public class LoginRequest {
    private String email;
    private String password;


}
