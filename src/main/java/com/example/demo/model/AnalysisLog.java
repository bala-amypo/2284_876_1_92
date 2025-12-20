package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class AnalysisLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    private HotspotZone zone;

    public void setZone(HotspotZone zone) {
        this.zone = zone;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
