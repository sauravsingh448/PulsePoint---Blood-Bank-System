package com.bloodbank.userService.service.Imp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bloodbank.security.CustomUserDetails;
import com.bloodbank.security.JwtProvider;
import com.bloodbank.userService.dto.ForgotPasswordRequest;
import com.bloodbank.userService.dto.ResetPasswordRequest;
import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.repository.UserRepository;
import com.bloodbank.userService.service.AuthServiceForgot;
import com.bloodbank.userService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceForgotImp implements AuthServiceForgot {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;


    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null){
            throw new RuntimeException("User not found");
        }
        //random unique token generate
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        // check password match
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        // encode password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // encode password
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public void sendOtpByPhone(String phone) {
        User user = userRepository.findByContact(phone);
        if(user == null) {
            throw new RuntimeException("User not found with this phone number");
        }
        // Rate Limiting Check
        if(user.getLastOtpSentTime() != null &&
                user.getLastOtpSentTime().plusSeconds(60).isAfter(LocalDateTime.now())){
            throw new RuntimeException("Please wait 60 seconds before requesting another OTP");
        }

        Random random = new Random();
        // create random 6 digits OTP
        int otp = 100000 + random.nextInt(900000);
        String generatedOtp = String.valueOf(otp); // convert into String
        user.setOtp(generatedOtp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        // Save last OTP time
        user.setLastOtpSentTime(LocalDateTime.now());
        userRepository.save(user);

        // send otp to email
        emailService.sendEmail(
                user.getEmail(),
                "PulsePoint - OTP Verification",
                "Dear User,\n\n" +
                        "Welcome to PulsePoint ❤️\n\n" +
                        "Your One-Time Password (OTP) for verification is: " + otp + "\n\n" +
                        "This OTP is valid for the next 5 minutes. Please do not share it with anyone for security reasons.\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Stay safe and thank you for being a part of PulsePoint – Saving Lives Together.\n\n" +
                        "Best Regards,\n" +
                        "Team PulsePoint Blood Bank"
        );
    }

    @Override
    public String verifyOtp(String phone, String otp) {
        User user = userRepository.findByContact(phone);

        if(user == null){
            throw new RuntimeException("User not found");
        }
        // check otp match
        if(!user.getOtp().equals(otp)){
            throw new RuntimeException("User not found");
        }
        // check expiry
        if(user.getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Invalid OTP");
        }
        // clear OTP after success
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        // Convert roles → authorities
        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Create CustomUserDetails
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        // Extract one role for JWT (optional but useful)
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName())
                .orElse("DONOR");

        // Generate JWT
        String token = jwtProvider.generateToken(userDetails, role);

        return token;
    }
}
