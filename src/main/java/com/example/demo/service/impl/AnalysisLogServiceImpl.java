package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AnalysisLogService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisLogServiceImpl implements AnalysisLogService {

    private final HotspotZoneRepository hotspotZoneRepository;
    private final CrimeReportRepository crimeReportRepository;
    private final PatternDetectionResultRepository patternDetectionResultRepository;
    private final AnalysisLogRepository analysisLogRepository;

    // ✅ EXACT constructor required by tests
    public AnalysisLogServiceImpl(
            HotspotZoneRepository hotspotZoneRepository,
            CrimeReportRepository crimeReportRepository,
            PatternDetectionResultRepository patternDetectionResultRepository,
            AnalysisLogRepository analysisLogRepository) {

        this.hotspotZoneRepository = hotspotZoneRepository;
        this.crimeReportRepository = crimeReportRepository;
        this.patternDetectionResultRepository = patternDetectionResultRepository;
        this.analysisLogRepository = analysisLogRepository;
    }

    // ✅ REQUIRED METHOD (tests call addLog)
    @Override
    public AnalysisLog addLog(Long zoneId, String message) {

        HotspotZone zone = hotspotZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        AnalysisLog log = new AnalysisLog();
        log.setZone(zone);
        log.setMessage(message);

        return analysisLogRepository.save(log);
    }

    // ✅ REQUIRED METHOD (tests call getLogsByZone)
    @Override
    public List<AnalysisLog> getLogsByZone(Long zoneId) {
        return analysisLogRepository.findByZoneId(zoneId);
    }
}
