package com.mateuszcholyn.monitoring.controller;


import com.mateuszcholyn.monitoring.service.MuseumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoringController {

    private final MuseumService museumService;

    public MonitoringController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("randomNumber/{bound}")
    public ResponseEntity<?> randomNumber(@PathVariable("bound") int bound) {
        return ResponseEntity.ok(museumService.randomNumber(bound));
    }

}
