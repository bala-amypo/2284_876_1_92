package com.example.demo.dto;

import java.time.LocalDateTime;

public class AnalysisLogDTO {

    private Long id;
    private Long zoneId;
    private String message;
    private LocalDateTime loggedAt;

    public AnalysisLogDTO() {}

    public AnalysisLogDTO(Long id, Long zoneId, String message, LocalDateTime loggedAt) {
        this.id = id;
        this.zoneId = zoneId;
        this.message = message;
        this.loggedAt = loggedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
