package com.bloodbank.security;

import com.bloodbank.userService.entity.Role;
import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.repository.RoleRepository;
import com.bloodbank.userService.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    // Generate jwt toke
    private final JwtProvider jwtProvider;
    // check user in DB
    private final UserRepository userRepository;
    // fetch role from DB
    private final RoleRepository roleRepository;
    // JSON response return karne ke liye
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        // 1. Get Google user
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 2. Check user exists
        User user = userRepository.findByEmail(email);

        // 3. Create new user if not exists
        if (user == null) {
            // Default role DB se fetch karo (DONOR)
            Role defaultRole = roleRepository.findByName("DONOR")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            // create new user object
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(null);
            user.setAddress("PENDING");
            user.setBloodGroup("PENDING");
            user.setContact("PENDING");
            user.setAge(0);
            user.setProfileCompleted(false);
            user.setProvider("GOOGLE");
            // Role assign karo (use Set for multiple roles)
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole);
            user.setRoles(roles);

            user = userRepository.save(user);
        }

        // 4. Convert DB Role → Spring Authority
        Set<GrantedAuthority> authorities = Set.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + user.getRoles().iterator().next().getName()
                )
        );

        // 5. Create CustomUserDetails
        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                null,
                authorities // role
        );

        // 6. Extract clean role (DONOR / ADMIN / RECIPIENT)
        String role = user.getRoles()
                .iterator()
                .next()
                .getName();

        // 7. Generate JWT
        String jwt = jwtProvider.generateToken(userDetails, role);

        // Frontend URL
        String redirectUrl =
                "https://medical-service-7ghq.vercel.app/oauth-success"
                        + "?token=" + jwt
                        + "&role=" + role
                        + "&profileCompleted=" + user.isProfileCompleted();

        // Redirect to React frontend
        response.sendRedirect(redirectUrl);
    }
}