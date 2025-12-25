package com.example.demo.controller;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.service.PatternDetectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patterns")
public class PatternDetectionController {

    private final PatternDetectionService patternService;

    public PatternDetectionController(PatternDetectionService patternService) {
        this.patternService = patternService;
    }

    @PostMapping("/detect/{zoneId}")
    public PatternDetectionResultDTO detect(@PathVariable Long zoneId) {
        return patternService.detectPattern(zoneId);
    }

    @GetMapping("/zone/{zoneId}")
    public List<PatternDetectionResultDTO> getByZone(@PathVariable Long zoneId) {
        return patternService.getResultsByZone(zoneId);
    }
}
