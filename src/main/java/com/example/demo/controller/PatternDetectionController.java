package com.example.demo.controller;

import com.example.demo.dto.PatternDetectionResultDTO;
import com.example.demo.service.PatternDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pattern")
public class PatternDetectionController {

    private final PatternDetectionService service;

    public PatternDetectionController(PatternDetectionService service) {
        this.service = service;
    }

    // Trigger pattern detection
    @PostMapping("/detect/{zoneId}")
    public ResponseEntity<PatternDetectionResultDTO> detectPattern(
            @PathVariable Long zoneId) {

        return ResponseEntity.ok(service.detectPattern(zoneId));
    }

    // Get results by zone
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<PatternDetectionResultDTO>> getResults(
            @PathVariable Long zoneId) {

        return ResponseEntity.ok(service.getResultsByZone(zoneId));
    }
}
