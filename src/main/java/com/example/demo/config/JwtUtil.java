// package com.example.demo.config;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import javax.crypto.SecretKey;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;

// @Component
// public class JwtUtil {
    
//     @Value("${jwt.secret}")
//     private String secret;
    
//     @Value("${jwt.expiration}")
//     private Long expiration;
    
//     private SecretKey getSigningKey() {
//         return Keys.hmacShaKeyFor(secret.getBytes());
//     }
    
//     public String generateToken(Long userId, String email, String role) {
//         Map<String, Object> claims = new HashMap<>();
//         claims.put("userId", userId);
//         claims.put("email", email);
//         claims.put("role", role);
        
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                 .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                 .compact();
//     }
    
//     public Claims parseToken(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(getSigningKey())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
    
//     public boolean isTokenExpired(String token) {
//         try {
//             Claims claims = parseToken(token);
//             return claims.getExpiration().before(new Date());
//         } catch (Exception e) {
//             return true;
//         }
//     }
// }