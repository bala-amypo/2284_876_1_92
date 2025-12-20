package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class AnalysisLogController {

    @PostMapping("/{zoneId}")
    public ResponseEntity<String> addLog(@PathVariable Long zoneId,
                                         @RequestBody Object log) {
        return ResponseEntity.ok("Analysis log added for zone " + zoneId);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<String> getLogs(@PathVariable Long zoneId) {
        return ResponseEntity.ok("Fetched analysis logs for zone " + zoneId);
    }
}
