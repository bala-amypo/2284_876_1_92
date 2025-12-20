package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.PatternDetectionService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatternDetectionServiceImpl implements PatternDetectionService {

    private final HotspotZoneRepository zoneRepository;
    private final CrimeReportRepository crimeRepository;
    private final PatternDetectionResultRepository resultRepository;
    private final AnalysisLogRepository logRepository;

    public PatternDetectionServiceImpl(
            HotspotZoneRepository zoneRepository,
            CrimeReportRepository crimeRepository,
            PatternDetectionResultRepository resultRepository,
            AnalysisLogRepository logRepository) {

        this.zoneRepository = zoneRepository;
        this.crimeRepository = crimeRepository;
        this.resultRepository = resultRepository;
        this.logRepository = logRepository;
    }

    @Override
    public PatternDetectionResult detectPattern(Long zoneId) {

        HotspotZone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Zone not found with id: " + zoneId));

        double lat = zone.getCenterLatitude();
        double lon = zone.getCenterLongitude();

        List<CrimeReport> nearbyCrimes =
                crimeRepository.findCrimesNear(lat, lon, 0.1);

        int count = nearbyCrimes.size();
        String pattern;

        if (count > 5) {
            pattern = "High";
            zone.setSeverity("HIGH");
        } else if (count > 0) {
            pattern = "Medium";
            zone.setSeverity("MEDIUM");
        } else {
            pattern = "No";
            zone.setSeverity("LOW");
        }

        zoneRepository.save(zone);

        PatternDetectionResult result = new PatternDetectionResult();
        result.setZone(zone);
        result.setCrimeCount(count);
        result.setPattern(pattern);

        PatternDetectionResult savedResult = resultRepository.save(result);

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage("Pattern detected: " + pattern + " (" + count + " crimes)");
        logRepository.save(log);

        return savedResult;
    }

    @Override
    public List<PatternDetectionResult> getResultsByZone(Long zoneId) {
        return resultRepository.findByZoneId(zoneId);
    }
}
