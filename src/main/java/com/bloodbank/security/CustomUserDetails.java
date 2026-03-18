package com.bloodbank.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Getter
public class CustomUserDetails implements UserDetails {

   private final Long userId;
   private final String username;
   private final String password;
   private final Set<GrantedAuthority> authorities;

   // Constructor matches your service
   public CustomUserDetails(Long userId,
                            String username,
                            String password,
                            Set<GrantedAuthority> authorities) {
      this.userId = userId;
      this.username = username;
      this.password = password;
      this.authorities = authorities;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return username;
   }

   @Override public boolean isAccountNonExpired() { return true; }
   @Override public boolean isAccountNonLocked() { return true; }
   @Override public boolean isCredentialsNonExpired() { return true; }
   @Override public boolean isEnabled() { return true; }
}
