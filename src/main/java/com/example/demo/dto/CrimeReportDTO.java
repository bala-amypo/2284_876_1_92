package com.example.demo.dto;

import java.time.LocalDateTime;

public class CrimeReportDTO {

    private Long id;
    private String crimeType;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime occurredAt;

    public CrimeReportDTO() {}

    public CrimeReportDTO(Long id, String crimeType, String description,
                          Double latitude, Double longitude, LocalDateTime occurredAt) {
        this.id = id;
        this.crimeType = crimeType;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.occurredAt = occurredAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}
