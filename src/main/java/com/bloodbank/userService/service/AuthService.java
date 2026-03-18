package com.bloodbank.userService.service;

import com.bloodbank.security.CustomUserDetails;
import com.bloodbank.security.JwtProvider;
import com.bloodbank.userService.dto.AuthResponse;
import com.bloodbank.userService.dto.LoginRequest;
import com.bloodbank.userService.repository.RoleRepository;
import com.bloodbank.userService.repository.UserRepository;
import com.bloodbank.userService.dto.RegisterRequest;
import com.bloodbank.userService.entity.Role;
import com.bloodbank.userService.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;

    public void registerUser(RegisterRequest request){
        // Check if email exists
        User isEmailExist = userRepository.findByEmail(request.getEmail());
        if(isEmailExist != null){
            throw new RuntimeException("Email is already used with another account..!" + request.getRole());
        }
        // create new User object (an empty user entity)
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBloodGroup(request.getBloodGroup());
        user.setContact(request.getContact());
        user.setAddress(request.getAddress());

        // Set to store user roles
        Set<Role> roles = new HashSet<>();

        // Fetch role from DB
        Role role = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid role!.."+ request.getRole())
                );

        // Add the selected role
        roles.add(role);

        //Assign roles to the user
        user.setRoles(roles);

        //save to DB
        userRepository.save(user);
    }

    // login section
    public AuthResponse login(LoginRequest request){

        // Authenticate the user
        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(username, password);

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        // 1. Get all roles assigned to logged-in user by Spring Security
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // 2. Extract first role (example: ROLE_DONOR)
        String springRole = authorities.isEmpty()
                ? null
                : authorities.iterator().next().getAuthority();

        // 3. Remove ROLE_ prefix to match DB role (DONOR / ADMIN / RECIPIENT)
        String role = springRole == null
                ? null
                : springRole.replace("ROLE_", "");

         // 4. Generate JWT token using email and clean role
        String jwt = jwtProvider.generateToken(userDetails, role);


        // Create a response object to send back to the client
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setMessage("Login successful...!");
        response.setRole(role);

        return response;
    }

    // Authenticate confirms the user before giving the access
    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid User..!");
        }

        // check the both password
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password...!");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
