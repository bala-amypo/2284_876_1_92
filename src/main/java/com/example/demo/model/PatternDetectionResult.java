package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class PatternDetectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int crimeCount;
    private String pattern;

    @ManyToOne
    private HotspotZone zone;

    public void setZone(HotspotZone zone) {
        this.zone = zone;
    }

    public void setCrimeCount(int crimeCount) {
        this.crimeCount = crimeCount;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
