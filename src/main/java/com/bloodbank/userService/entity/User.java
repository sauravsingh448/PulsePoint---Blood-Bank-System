package com.bloodbank.userService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "reset_token", nullable = true)
    private String resetToken;

    @Column(name = "token_expiry", nullable = true)
    private LocalDateTime tokenExpiry;

    @Column(nullable = true)
    private String otp;

    @Column(nullable = true)
    private LocalDateTime otpExpiry;

    @Column(name = "last_otp_sent_time")
    private LocalDateTime lastOtpSentTime;  // when last opt send

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Column mapping to User entity
            inverseJoinColumns = @JoinColumn(name = "role_id") // Column mapping to Role entity
    )
    private Set<Role> roles = new HashSet<>(); // User roles stored here
}

