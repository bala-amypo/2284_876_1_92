package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_logs")
public class AnalysisLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private HotspotZone zone;

    private String message;

    private LocalDateTime loggedAt;

    public AnalysisLog() {}

    public AnalysisLog(HotspotZone zone, String message) {
        this.zone = zone;
        this.message = message;
        this.loggedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (loggedAt == null) {
            loggedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public HotspotZone getZone() {
        return zone;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }
}
