package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class CrimeReportController {

    @PostMapping
    public ResponseEntity<String> addCrimeReport(@RequestBody Object crimeReport) {
        return ResponseEntity.ok("Crime report added successfully");
    }

    @GetMapping
    public ResponseEntity<String> getAllReports() {
        return ResponseEntity.ok("Fetched all crime reports");
    }
}
