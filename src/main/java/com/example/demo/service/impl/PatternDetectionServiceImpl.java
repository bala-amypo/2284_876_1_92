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
            AnalysisLogRepository logRepo
    ) {
        this.zoneRepo = zoneRepo;
        this.reportRepo = reportRepo;
        this.resultRepo = resultRepo;
        this.logRepo = logRepo;
    }

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        double range = 0.1;

        List<CrimeReport> crimes =
                reportRepo.findByLatLongRange(
                        zone.getCenterLat() - range,
                        zone.getCenterLat() + range,
                        zone.getCenterLong() - range,
                        zone.getCenterLong() + range
                );

        int count = crimes.size();

        String severity;
        if (count >= 10) severity = "HIGH CRIME";
        else if (count >= 5) severity = "MEDIUM CRIME";
        else if (count > 0) severity = "LOW CRIME";
        else severity = "NO CRIME";

        zone.setSeverityLevel(severity);
        zoneRepo.save(zone);

        PatternDetectionResult entity = new PatternDetectionResult();
        entity.setZone(zone);
        entity.setCrimeCount(count);
        entity.setDetectedPattern(severity);
        entity.setAnalysisDate(LocalDate.now());

        PatternDetectionResult saved = resultRepo.save(entity);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detection completed: " + severity);
        logRepo.save(log);

        return toDTO(saved);
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
                r.getZone().getId(),
                r.getCrimeCount(),
                r.getDetectedPattern(),
                r.getAnalysisDate()
        );
    }
}
