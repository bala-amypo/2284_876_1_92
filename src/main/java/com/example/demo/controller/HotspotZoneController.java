package com.example.demo.controller;

import com.example.demo.model.HotspotZone;
import com.example.demo.service.HotspotZoneService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/zones")
public class HotspotZoneController {

    private final HotspotZoneService service;

    public HotspotZoneController(HotspotZoneService service) {
        this.service = service;
    }

    @PostMapping
    public HotspotZone create(@RequestBody HotspotZone zone) {
        return service.addZone(zone);
    }

    @GetMapping
    public List<HotspotZone> all() {
        return service.getAllZones();
    }
}
