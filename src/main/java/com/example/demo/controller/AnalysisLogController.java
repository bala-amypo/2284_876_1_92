package com.example.demo.controller;

import com.example.demo.model.PatternDetectionResult;
import com.example.demo.repository.PatternDetectionResultRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis-logs")
public class AnalysisLogController {

    private final PatternDetectionResultRepository resultRepository;

    public AnalysisLogController(PatternDetectionResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @GetMapping("/zone/{zoneId}")
    public List<PatternDetectionResult> getLogsByZone(@PathVariable Long zoneId) {
        try {
            return resultRepository.findByZone_Id(zoneId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch analysis logs");
        }
    }
}
