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

    private final HotspotZoneRepository hotspotZoneRepository;
    private final CrimeReportRepository crimeReportRepository;
    private final PatternDetectionResultRepository patternDetectionResultRepository;
    private final AnalysisLogRepository analysisLogRepository;

    // ✅ MUST MATCH TEST CONSTRUCTOR
    public PatternDetectionServiceImpl(
            HotspotZoneRepository hotspotZoneRepository,
            CrimeReportRepository crimeReportRepository,
            PatternDetectionResultRepository patternDetectionResultRepository,
            AnalysisLogRepository analysisLogRepository) {

        this.hotspotZoneRepository = hotspotZoneRepository;
        this.crimeReportRepository = crimeReportRepository;
        this.patternDetectionResultRepository = patternDetectionResultRepository;
        this.analysisLogRepository = analysisLogRepository;
    }

    @Override
    public PatternDetectionResultDTO detectPattern(Long zoneId) {

        HotspotZone zone = hotspotZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        double lat = zone.getCenterLat();
        double lon = zone.getCenterLong();

        List<CrimeReport> crimes =
                crimeReportRepository.findByLatLongRange(
                        lat - 0.1, lat + 0.1,
                        lon - 0.1, lon + 0.1
                );

        int crimeCount = crimes.size();

        String pattern =
                crimeCount > 10 ? "HIGH_RISK" :
                crimeCount > 5  ? "MEDIUM_RISK" :
                                  "LOW_RISK";

        PatternDetectionResult result =
                new PatternDetectionResult(
                        zone,
                        LocalDate.now(),
                        crimeCount,
                        pattern
                );

        result = patternDetectionResultRepository.save(result);

        // ✅ REQUIRED log save
        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detected: " + pattern);
        analysisLogRepository.save(log);

        // ✅ CORRECT DTO CONSTRUCTOR ORDER
        return new PatternDetectionResultDTO(
                zone.getId(),                 // Long
                result.getId(),               // Long
                result.getAnalysisDate(),     // LocalDate
                result.getCrimeCount(),       // Integer
                result.getDetectedPattern()   // String
        );
    }

    @Override
    public List<PatternDetectionResultDTO> getResultsByZone(Long zoneId) {

        return patternDetectionResultRepository.findByZone_Id(zoneId)
                .stream()
                .map(r -> new PatternDetectionResultDTO(
                        r.getZone().getId(),
                        r.getId(),
                        r.getAnalysisDate(),
                        r.getCrimeCount(),
                        r.getDetectedPattern()
                ))
                .collect(Collectors.toList());
    }
}
