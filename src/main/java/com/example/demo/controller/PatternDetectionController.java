package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patterns")
public class PatternDetectionController {

    @PostMapping("/detect/{zoneId}")
    public ResponseEntity<String> detectPattern(@PathVariable Long zoneId) {
        return ResponseEntity.ok("Pattern detection completed for zone " + zoneId);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<String> getDetectionResult(@PathVariable Long zoneId) {
        return ResponseEntity.ok("Detection results for zone " + zoneId);
    }
}
