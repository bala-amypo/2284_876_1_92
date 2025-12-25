package com.example.demo.controller;

import com.example.demo.model.PatternDetectionResult;
import com.example.demo.service.PatternDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patterns")
public class PatternDetectionController {

    private final PatternDetectionService patternDetectionService;

    public PatternDetectionController(PatternDetectionService patternDetectionService) {
        this.patternDetectionService = patternDetectionService;
    }

    @PostMapping("/detect/{zoneId}")
    public ResponseEntity<PatternDetectionResult> detect(@PathVariable Long zoneId) {
        return ResponseEntity.ok(patternDetectionService.detectPattern(zoneId));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<PatternDetectionResult>> getByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(patternDetectionService.getResultsByZone(zoneId));
    }
}
