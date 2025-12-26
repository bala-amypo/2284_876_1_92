package com.example.demo.controller;

import com.example.demo.model.HotspotZone;
import com.example.demo.service.HotspotZoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zones")
public class HotspotZoneController {

    private final HotspotZoneService zoneService;

    public HotspotZoneController(HotspotZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public HotspotZone createZone(@RequestBody HotspotZone zone) {
        return zoneService.addZone(zone);
    }

    @GetMapping
    public List<HotspotZone> getAllZones() {
        return zoneService.getAllZones();
    }
}
