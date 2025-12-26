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

        int crimeCount = crimes.size();

        String pattern;
        if (crimeCount >= 10) pattern = "HIGH CRIME";
        else if (crimeCount >= 5) pattern = "MEDIUM CRIME";
        else if (crimeCount > 0) pattern = "LOW CRIME";
        else pattern = "NO CRIME";

        zone.setSeverityLevel(pattern);
        zoneRepo.save(zone);

        PatternDetectionResult result = new PatternDetectionResult();
        result.setZone(zone);
        result.setCrimeCount(crimeCount);
        result.setDetectedPattern(pattern);
        result.setAnalysisDate(LocalDate.now());

        PatternDetectionResult saved = resultRepo.save(result);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detection completed: " + pattern);
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

    // 🔥 FIXED CONSTRUCTOR ORDER
    private PatternDetectionResultDTO toDTO(PatternDetectionResult r) {
        return new PatternDetectionResultDTO(
                r.getId(),
                r.getZone().getId(),
                r.getAnalysisDate(),     // ✅ LocalDate FIRST
                r.getCrimeCount(),       // ✅ Integer AFTER
                r.getDetectedPattern()
        );
    }
}
