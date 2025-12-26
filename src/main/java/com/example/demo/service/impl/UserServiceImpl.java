package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User register(User user) {
        if (repo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Simple password encoding (NO Spring Security needed)
        String encoded =
                Base64.getEncoder().encodeToString(
                        user.getPassword().getBytes()
                );

        user.setPassword(encoded);

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ANALYST");
        }

        return repo.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
