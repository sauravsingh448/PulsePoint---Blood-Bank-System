package com.bloodbank.security;

import com.bloodbank.userService.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // Public
                        //.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/forgot-password").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        // Donor APIs (JWT required)
                        .requestMatchers("/api/donor/**").hasRole("DONOR")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/recipient/**").hasRole("RECIPIENT")
                        .requestMatchers("/api/**").hasAnyRole("DONOR", "RECIPIENT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customerUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}


/*

{
        "name": "Saurav",
        "email": "saurav123@gmail.com",
        "password": "sau1234",
        "bloodGroup": "A+",
        "contact": "9999999999",
        "address": "Mumbai",
        "role": "DONOR"

     "name": "saurav kumar singh",
    "bloodGroup": "A+",
    "contact": "9835729590",
    "address": "Bihar"


        "name": "Rahul",
    "email": "rahul123@gmail.com",
    "password": "rahul123",
    "bloodGroup": "A+",
    "contact": "9835729982",
    "address": "Mumbai",
    "role": "ADMIN"



     "name": "Rohan",
    "email": "rohan123@gmail.com",
    "password": "rahul123",
    "bloodGroup": "A+",
    "contact": "9835729982",
    "address": "Delhi",
    "role": "RECIPIENT"
        }

 */