package com.example.demo.service.impl;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PatternDetectionService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PatternDetectionServiceImpl implements PatternDetectionService {

    private final HotspotZoneRepository zoneRepo;
    private final CrimeReportRepository reportRepo;
    private final PatternDetectionResultRepository resultRepo;
    private final AnalysisLogRepository logRepo;

    public PatternDetectionServiceImpl(
            HotspotZoneRepository zoneRepo,
            CrimeReportRepository reportRepo,
            PatternDetectionResultRepository resultRepo,
            AnalysisLogRepository logRepo) {

        this.zoneRepo = zoneRepo;
        this.reportRepo = reportRepo;
        this.resultRepo = resultRepo;
        this.logRepo = logRepo;
    }

    // ===============================
    // detectPattern → RETURNS DTO
    // ===============================
    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        List<CrimeReport> reports =
                reportRepo.findByLatLongRange(
                        zone.getCenterLat() - 0.1,
                        zone.getCenterLat() + 0.1,
                        zone.getCenterLong() - 0.1,
                        zone.getCenterLong() + 0.1
                );

        int count = reports.size();
        String pattern;

        if (count == 0) pattern = "No Crime";
        else if (count < 5) pattern = "Low Crime";
        else if (count < 10) pattern = "Medium Crime";
        else pattern = "High Crime";

        // ---- Save ENTITY ----
        PatternDetectionResult entity = new PatternDetectionResult();
        entity.setZone(zone);
        entity.setCrimeCount(count);
        entity.setDetectedPattern(pattern);
        entity.setAnalysisDate(LocalDate.now());

        resultRepo.save(entity);

        // ---- Log ----
        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detected: " + pattern);
        logRepo.save(log);

        // ---- Update severity ----
        zone.setSeverityLevel(
                pattern.contains("High") ? "HIGH" :
                pattern.contains("Medium") ? "MEDIUM" : "LOW"
        );
        zoneRepo.save(zone);

        // ---- Convert ENTITY → DTO ----
        return toDTO(entity);
    }

    // ==========================================
    // getResultsByZone → RETURNS DTO LIST
    // ==========================================
    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // ENTITY → DTO mapper
    // ===============================
    private PatternDetectionResultDTO toDTO(PatternDetectionResult e) {

        return new PatternDetectionResultDTO(
                e.getId(),
                e.getZone().getId(),
                e.getCrimeCount(),
                e.getDetectedPattern(),
                e.getAnalysisDate()
        );
    }
}
