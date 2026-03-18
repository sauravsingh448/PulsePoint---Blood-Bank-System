package com.bloodbank.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String SECRET = "gasavjwavcjaevfjedvkjasckawcvsdzchjdschsgdfvsdjhhsdcsdjcvsdjc"; // 32+ chars
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    //private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private static final long EXPIRATION_TIME = 1000 * 60 * 20;

    // Generate JWT Token
    public String generateToken(CustomUserDetails userDetails, String role) {
        //System.out.println("JWT SECRET LENGTH = " + SECRET.length());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userId", userDetails.getUserId())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract userId
    public Long extractUserId(String token){
        return extractClaims(token).get("userId", Long.class);
    }

    // Extract userRole
    public String extractUserRole(String token){
        return extractClaims(token).get("role", String.class);
    }


    // Extract all claims with exception handling
    private Claims extractClaims(String token) {
        try {

            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("Token is missing");
            }

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);

        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid token format", e);

        } catch (SignatureException e) {
            throw new RuntimeException("Token signature mismatch", e);

        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Unsupported JWT token", e);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token is empty or null", e);
        }
    }

    // Validate token
    public boolean validateToken(String token){
        return !isTokenExpired(token);
    }

    // Check if token expired
    private boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // If any error → treat token as expired
        }
    }
}
