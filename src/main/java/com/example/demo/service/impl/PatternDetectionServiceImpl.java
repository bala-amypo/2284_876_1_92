package com.example.demo.service.impl;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.model.AnalysisLog;
import com.example.demo.model.CrimeReport;
import com.example.demo.model.HotspotZone;
import com.example.demo.model.PatternDetectionResult;
import com.example.demo.repository.AnalysisLogRepository;
import com.example.demo.repository.CrimeReportRepository;
import com.example.demo.repository.HotspotZoneRepository;
import com.example.demo.repository.PatternDetectionResultRepository;
import com.example.demo.service.PatternDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    // ===================== DETECT PATTERN =====================
    @Override
   public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)

      orElseThrow(() -> new RuntimeException("Zone not found"));

        double minLat = zone.getCenterLat() - 0.1;
        double maxLat = zone.getCenterLat() + 0.1;
        double minLong = zone.getCenterLong() - 0.1;
        double maxLong = zone.getCenterLong() + 0.1;

        List<CrimeReport> crimes =
                reportRepo.findByLatLongRange(minLat, maxLat, minLong, maxLong);

        int crimeCount = crimes.size();

        String detectedPattern;
        String newSeverity;

        if (crimeCount > 5) {
            detectedPattern = "High Risk Pattern Detected";
            newSeverity = "HIGH";
        } else if (crimeCount > 0) {
            detectedPattern = "Medium Risk Pattern Detected";
            newSeverity = "MEDIUM";
        } else {
            detectedPattern = "No Pattern Detected";
            newSeverity = "LOW";
        }

        PatternDetectionResult result = new PatternDetectionResult(
                zone,
                LocalDate.now(),
                crimeCount,
                detectedPattern
        );

        result = resultRepo.save(result);

        // update zone severity
        zone.setSeverityLevel(newSeverity);
        zoneRepo.save(zone);

        // save analysis log
        AnalysisLog log = new AnalysisLog(
                "Pattern detection completed for zone: " + zone.getZoneName(),
                null,
                zone
        );
        logRepo.save(log);

        // return DTO
        return new PatternDetectionResultDTO(
                result.getId(),
                zone.getId(),
                result.getAnalysisDate(),
                result.getCrimeCount(),
                result.getDetectedPattern()
        );
    }

    // ===================== GET RESULTS BY ZONE =====================
    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(result -> new PatternDetectionResultDTO(
                        result.getId(),
                        result.getZone().getId(),
                        result.getAnalysisDate(),
                        result.getCrimeCount(),
                        result.getDetectedPattern()
                ))
                .toList();
    }
}
