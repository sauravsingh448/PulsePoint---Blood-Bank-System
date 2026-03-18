package com.bloodbank.userService.controller;

import com.bloodbank.userService.dto.*;
import com.bloodbank.userService.service.AuthService;
import com.bloodbank.userService.service.AuthServiceForgot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthServiceForgot authServiceForgot;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.registerUser(request);
        }catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.ok("User register successfully!..");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) throws Exception{
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request
            ){
        authServiceForgot.forgotPassword(request);
        return ResponseEntity.ok("Reset password link sent to email");
    }

    // Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request
            ){
        authServiceForgot.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phone){
        authServiceForgot.sendOtpByPhone(phone);
        return ResponseEntity.ok("OTP sent to registered email");
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String phone,
            @RequestParam String otp
    ){
        String token = authServiceForgot.verifyOtp(phone,otp);
        return ResponseEntity.ok().body(
                Map.of(
                        "message", "OTP verified successfully",
                        "token", token
                )
        ); // JWT return
    }
}
