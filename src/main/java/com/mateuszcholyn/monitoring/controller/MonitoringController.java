package com.mateuszcholyn.monitoring.controller;


import com.mateuszcholyn.monitoring.service.MuseumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    private final MuseumService museumService;

    public MonitoringController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("enterMuseum")
    public ResponseEntity<?> hello() {
        museumService.visitMuseum();
        return ResponseEntity.ok().build();
    }

}
