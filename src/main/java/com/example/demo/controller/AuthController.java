package com.example.demo.controller;

// import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {
    
    private final UserService userService;
    // private final JwtUtil jwtUtil;
    // private final PasswordEncoder passwordEncoder;
    
    // public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
    //     this.userService = userService;
    //     this.jwtUtil = jwtUtil;
    //     this.passwordEncoder = passwordEncoder;
    // }
    
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail());
            // if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            //     String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
            //     return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getRole()));
            // } else {
            //     return ResponseEntity.status(401).build();
            // }
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}