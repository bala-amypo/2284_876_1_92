package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zones")
public class HotspotZoneController {

    @PostMapping
    public ResponseEntity<String> createZone(@RequestBody Object zone) {
        return ResponseEntity.ok("Hotspot zone created");
    }

    @GetMapping
    public ResponseEntity<String> getZones() {
        return ResponseEntity.ok("Fetched hotspot zones");
    }
}
