package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "zoneName")
})
public class HotspotZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String zoneName;

    private Double centerLat;
    private Double centerLong;

    // LOW / MEDIUM / HIGH
    private String severityLevel;

    // Getters & Setters
}
