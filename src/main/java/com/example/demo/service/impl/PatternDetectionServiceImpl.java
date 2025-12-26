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

        double minLat = zone.getCenterLat() - 0.1;
        double maxLat = zone.getCenterLat() + 0.1;
        double minLon = zone.getCenterLong() - 0.1;
        double maxLon = zone.getCenterLong() + 0.1;

        List<CrimeReport> crimes =
                reportRepo.findByLatLongRange(minLat, maxLat, minLon, maxLon);

        int count = crimes.size();
        String pattern;
        String severity;

        if (count > 5) {
            pattern = "High Risk Pattern Detected";
            severity = "HIGH";
        } else if (count > 0) {
            pattern = "Medium Risk Pattern Detected";
            severity = "MEDIUM";
        } else {
            pattern = "No Pattern Detected";
            severity = "LOW";
        }

        PatternDetectionResult result =
                new PatternDetectionResult(zone, LocalDate.now(), count, pattern);

        resultRepo.save(result);

        zone.setSeverityLevel(severity);
        zoneRepo.save(zone);

        logRepo.save(new AnalysisLog(zone, pattern));

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
                        r.getDetectedPattern()))
                .collect(Collectors.toList());
    }
}
