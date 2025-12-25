package com.example.demo.controller;

import com.example.demo.model.HotspotZone;
import com.example.demo.service.HotspotZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zones")
public class HotspotZoneController {

    private final HotspotZoneService hotspotZoneService;

    public HotspotZoneController(HotspotZoneService hotspotZoneService) {
        this.hotspotZoneService = hotspotZoneService;
    }

    @PostMapping
    public ResponseEntity<HotspotZone> addZone(@RequestBody HotspotZone zone) {
        return ResponseEntity.ok(hotspotZoneService.addZone(zone));
    }

    @GetMapping
    public ResponseEntity<List<HotspotZone>> getAllZones() {
        return ResponseEntity.ok(hotspotZoneService.getAllZones());
    }
}
