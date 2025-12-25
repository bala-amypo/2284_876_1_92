package com.example.demo.service.impl;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PatternDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatternDetectionServiceImpl implements PatternDetectionService {

    private final HotspotZoneRepository zoneRepo;
    private final CrimeReportRepository reportRepo;
    private final PatternDetectionResultRepository resultRepo;

    public PatternDetectionServiceImpl(
            HotspotZoneRepository zoneRepo,
            CrimeReportRepository reportRepo,
            PatternDetectionResultRepository resultRepo) {

        this.zoneRepo = zoneRepo;
        this.reportRepo = reportRepo;
        this.resultRepo = resultRepo;
    }

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        double minLat = zone.getCenterLat() - 0.1;
        double maxLat = zone.getCenterLat() + 0.1;
        double minLong = zone.getCenterLong() - 0.1;
        double maxLong = zone.getCenterLong() + 0.1;

        List<CrimeReport> crimes =
                reportRepo.findByLatLongRange(minLat, maxLat, minLong, maxLong);

        int crimeCount = crimes.size();

        String pattern;
        String severity;

        if (crimeCount > 5) {
            pattern = "High Risk Pattern Detected";
            severity = "HIGH";
        } else if (crimeCount > 0) {
            pattern = "Medium Risk Pattern Detected";
            severity = "MEDIUM";
        } else {
            pattern = "No Pattern Detected";
            severity = "LOW";
        }

        PatternDetectionResult result =
                new PatternDetectionResult(zone, LocalDate.now(), crimeCount, pattern);

        result = resultRepo.save(result);

        zone.setSeverityLevel(severity);
        zoneRepo.save(zone);

        return new PatternDetectionResultDTO(
                result.getId(),
                zone.getId(),
                result.getAnalysisDate(),
                result.getCrimeCount(),
                result.getDetectedPattern()
        );
    }

    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(r -> new PatternDetectionResultDTO(
                        r.getId(),
                        r.getZone().getId(),
                        r.getAnalysisDate(),
                        r.getCrimeCount(),
                        r.getDetectedPattern()
                ))
                .toList();
    }
}
