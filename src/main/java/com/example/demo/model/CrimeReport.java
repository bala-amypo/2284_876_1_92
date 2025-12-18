package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
public class CrimeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String crimeType;

    private String description;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    private LocalDateTime occurredAt;

    @PrePersist
    public void validateDate() {
        if (occurredAt.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Crime date cannot be in the future");
        }
    }

    // Getters & Setters
}
