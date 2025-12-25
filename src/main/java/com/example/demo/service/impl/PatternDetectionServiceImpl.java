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
import java.util.stream.Collectors;

@Service
public class PatternDetectionServiceImpl implements PatternDetectionService {

    private final CrimeReportRepository crimeRepo;
    private final HotspotZoneRepository zoneRepo;
    private final PatternDetectionResultRepository resultRepo;
    private final AnalysisLogRepository logRepo;

    public PatternDetectionServiceImpl(
            CrimeReportRepository crimeRepo,
            HotspotZoneRepository zoneRepo,
            PatternDetectionResultRepository resultRepo,
            AnalysisLogRepository logRepo
    ) {
        this.crimeRepo = crimeRepo;
        this.zoneRepo = zoneRepo;
        this.resultRepo = resultRepo;
        this.logRepo = logRepo;
    }

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepo.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        double lat = zone.getCenterLat();
        double lon = zone.getCenterLong();

        // Simple bounding box logic
        List<CrimeReport> crimes = crimeRepo.findByLatLongRange(
                lat - 0.01, lat + 0.01,
                lon - 0.01, lon + 0.01
        );

        int crimeCount = crimes.size();

        String pattern;
        if (crimeCount > 20) {
            pattern = "HIGH_RISK_PATTERN";
            zone.setSeverityLevel("HIGH");
        } else if (crimeCount > 10) {
            pattern = "MEDIUM_RISK_PATTERN";
            zone.setSeverityLevel("MEDIUM");
        } else {
            pattern = "LOW_RISK_PATTERN";
            zone.setSeverityLevel("LOW");
        }

        zoneRepo.save(zone);

        PatternDetectionResult result = new PatternDetectionResult(
                zone,
                LocalDate.now(),
                crimeCount,
                pattern
        );

        resultRepo.save(result);

        // ✅ Correct logging
        logRepo.save(new AnalysisLog(
                "Pattern detected for zone: " + zone.getZoneName() +
                        " | Pattern: " + pattern +
                        " | Crime Count: " + crimeCount,
                zone
        ));

        return mapToDTO(result);
    }

    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return resultRepo.findByZone_Id(zoneId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PatternDetectionResultDTO mapToDTO(PatternDetectionResult result) {
        return new PatternDetectionResultDTO(
                result.getId(),
                result.getZone().getZoneName(),
                result.getAnalysisDate(),
                result.getCrimeCount(),
                result.getDetectedPattern()
        );
    }
}
