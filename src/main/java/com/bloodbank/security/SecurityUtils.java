package com.bloodbank.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        // Get the principal (user details) from Authentication
        // Cast it to your custom UserDetails implementation
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return user.getUserId();
    }
}
