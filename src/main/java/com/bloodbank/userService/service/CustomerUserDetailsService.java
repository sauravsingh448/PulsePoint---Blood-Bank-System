package com.bloodbank.userService.service;

import com.bloodbank.security.CustomUserDetails;
import com.bloodbank.userService.entity.Role;
import com.bloodbank.userService.entity.User;
import com.bloodbank.userService.repository.RoleRepository;
import com.bloodbank.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // This method is called by Spring Security when a user tries to log in.
    //  It loads the user from the database using email.


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //  Fetch user from DB using email
        User user = userRepository.findByEmail(email);

        // If user NOT found → throw exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // Load roles of the user from user_roles table
        Set<Role> roles = user.getRoles();
        // Convert Role objects to Spring Security Authorities

        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        // Return Spring Security's built-in User object with email, password, and roles
        // Wrap everything into CustomUserDetails
        return new CustomUserDetails(
                user.getId(),           // USER ID SAFE
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
