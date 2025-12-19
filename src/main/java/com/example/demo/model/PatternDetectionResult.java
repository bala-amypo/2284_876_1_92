package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PatternDetectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private HotspotZone zone;

    private LocalDate analysisDate;

    private Integer crimeCount;

    private String detectedPattern;

    @PrePersist
    public void validateCount() {
        if (crimeCount < 0) {
            throw new RuntimeException("Crime count cannot be negative");
        }
    }

    // Getters & Setters
}
