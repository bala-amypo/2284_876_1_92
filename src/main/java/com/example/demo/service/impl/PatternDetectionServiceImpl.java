package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PatternDetectionService;

import java.time.LocalDate;
import java.util.List;

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
    public PatternDetectionResult detectPattern(Long zoneId) {

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

        PatternDetectionResult result = new PatternDetectionResult();
        result.setZone(zone);
        result.setCrimeCount(count);
        result.setDetectedPattern(pattern);
        result.setAnalysisDate(LocalDate.now());

        resultRepo.save(result);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detected: " + pattern);
        logRepo.save(log);

        zone.setSeverityLevel(pattern.contains("High") ? "HIGH" :
                              pattern.contains("Medium") ? "MEDIUM" : "LOW");

        zoneRepo.save(zone);

        return result;
    }

    @Override
    public List<PatternDetectionResult> getResultsByZone(Long zoneId) {
        return resultRepo.findByZone_Id(zoneId);
    }
}
