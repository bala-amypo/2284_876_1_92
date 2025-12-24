package com.example.demo.dto;

import java.time.LocalDate;

public class PatternDetectionResultDTO {

    private Long id;
    private Long zoneId;
    private LocalDate analysisDate;
    private Integer crimeCount;
    private String detectedPattern;

    public PatternDetectionResultDTO() {}

    public PatternDetectionResultDTO(Long id, Long zoneId,
                                     LocalDate analysisDate, Integer crimeCount,
                                     String detectedPattern) {
        this.id = id;
        this.zoneId = zoneId;
        this.analysisDate = analysisDate;
        this.crimeCount = crimeCount;
        this.detectedPattern = detectedPattern;
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

    public LocalDate getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDate analysisDate) {
        this.analysisDate = analysisDate;
    }

    public Integer getCrimeCount() {
        return crimeCount;
    }

    public void setCrimeCount(Integer crimeCount) {
        this.crimeCount = crimeCount;
    }

    public String getDetectedPattern() {
        return detectedPattern;
    }

    public void setDetectedPattern(String detectedPattern) {
        this.detectedPattern = detectedPattern;
    }
}
