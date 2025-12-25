package com.example.demo.service.impl;

import com.example.demo.model.AnalysisLog;
import com.example.demo.model.HotspotZone;
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

    // 🔥 Constructor EXACTLY as test expects
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

    @Override
    public AnalysisLog addLog(Long zoneId, String message) {

        HotspotZone zone = hotspotZoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        AnalysisLog log = new AnalysisLog(zone, message);
        return analysisLogRepository.save(log);
    }

    @Override
    public List<AnalysisLog> getLogsByZone(Long zoneId) {
        return analysisLogRepository.findByZone_Id(zoneId);
    }
}
