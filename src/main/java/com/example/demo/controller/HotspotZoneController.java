package com.example.demo.controller;

import com.example.demo.model.HotspotZone;
import com.example.demo.service.HotspotZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/zones")
@Tag(name = "Hotspot Zones")
public class HotspotZoneController {
    
    private final HotspotZoneService hotspotZoneService;
    
    public HotspotZoneController(HotspotZoneService hotspotZoneService) {
        this.hotspotZoneService = hotspotZoneService;
    }
    
    @PostMapping
    @Operation(summary = "Create hotspot zone")
    public ResponseEntity<HotspotZone> addZone(@RequestBody HotspotZone zone) {
        try {
            HotspotZone savedZone = hotspotZoneService.addZone(zone);
            return ResponseEntity.status(201).body(savedZone);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all hotspot zones")
    public ResponseEntity<List<HotspotZone>> getAllZones() {
        List<HotspotZone> zones = hotspotZoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }
}