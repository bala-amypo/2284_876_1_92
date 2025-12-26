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

        double delta = 0.05;

        List<CrimeReport> crimes =
                reportRepo.findByLatLongRange(
                        zone.getCenterLat() - delta,
                        zone.getCenterLat() + delta,
                        zone.getCenterLong() - delta,
                        zone.getCenterLong() + delta
                );

        int count = crimes.size();

        String pattern;
        String severity;

        if (count >= 10) {
            pattern = "High crime activity detected";
            severity = "HIGH";
        } else if (count >= 5) {
            pattern = "Medium crime activity detected";
            severity = "MEDIUM";
        } else if (count > 0) {
            pattern = "Low crime activity detected";
            severity = "LOW";
        } else {
            pattern = "No crime pattern detected";
            severity = "LOW";
        }

        zone.setSeverityLevel(severity);
        zoneRepo.save(zone);

        PatternDetectionResult result = new PatternDetectionResult();
        result.setZone(zone);
        result.setCrimeCount(count);
        result.setDetectedPattern(pattern);
        result.setAnalysisDate(LocalDate.now());

        resultRepo.save(result);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage(pattern);
        logRepo.save(log);

        return toDTO(result);
    }

    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {
        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PatternDetectionResultDTO toDTO(PatternDetectionResult r) {
        return new PatternDetectionResultDTO(
                r.getId(),
                r.getZone() != null ? r.getZone().getId() : null,
                r.getAnalysisDate(),
                r.getCrimeCount(),
                r.getDetectedPattern()
        );
    }
}
