package com.example.demo.service.impl;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PatternDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        double radius = 0.1;
        List<CrimeReport> crimes = reportRepo.findByLatLongRange(
                zone.getCenterLat() - radius,
                zone.getCenterLat() + radius,
                zone.getCenterLong() - radius,
                zone.getCenterLong() + radius
        );

        int count = crimes.size();
        String pattern;

        if (count >= 10) {
            pattern = "HIGH RISK ZONE";
            zone.setSeverityLevel("HIGH");
        } else if (count >= 5) {
            pattern = "MEDIUM RISK ZONE";
            zone.setSeverityLevel("MEDIUM");
        } else {
            pattern = "NO SIGNIFICANT PATTERN";
            zone.setSeverityLevel("LOW");
        }

        zoneRepo.save(zone);

        PatternDetectionResult result = new PatternDetectionResult(
                zone,
                LocalDate.now(),
                count,
                pattern
        );

        resultRepo.save(result);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detected: " + pattern);
        logRepo.save(log);

        return new PatternDetectionResultDTO(
                result.getId(),
                zone.getId(),
                result.getAnalysisDate(),
                result.getCrimeCount(),
                result.getDetectedPattern()
        );
    }

    // 🔥 THIS METHOD FIXES BOTH FAILING TEST CASES
    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(r -> new PatternDetectionResultDTO(
                        r.getId(),
                        r.getZone() != null ? r.getZone().getId() : zoneId,
                        r.getAnalysisDate(),
                        r.getCrimeCount(),
                        r.getDetectedPattern()
                ))
                .collect(Collectors.toList());
    }
}
