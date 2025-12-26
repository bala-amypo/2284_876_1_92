package com.example.demo.model;

import java.time.LocalDateTime;

public class AnalysisLog {

    private Long id;
    private String message;
    private HotspotZone zone;
    private LocalDateTime loggedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public HotspotZone getZone() { return zone; }
    public void setZone(HotspotZone zone) { this.zone = zone; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
}
