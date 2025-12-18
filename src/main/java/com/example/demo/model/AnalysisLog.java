package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AnalysisLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime loggedAt;

    @ManyToOne
    private HotspotZone zone;

    @PrePersist
    public void prePersist() {
        this.loggedAt = LocalDateTime.now();
    }

    // Getters & Setters
}
