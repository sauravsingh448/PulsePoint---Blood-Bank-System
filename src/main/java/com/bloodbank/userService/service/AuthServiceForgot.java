package com.bloodbank.userService.service;

import com.bloodbank.userService.dto.ForgotPasswordRequest;
import com.bloodbank.userService.dto.ResetPasswordRequest;

public interface AuthServiceForgot {
    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);


    // login with phone number
    void sendOtpByPhone(String phone);
    String verifyOtp(String phone, String otp);
}
