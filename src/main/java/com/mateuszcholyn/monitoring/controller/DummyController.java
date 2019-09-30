package com.mateuszcholyn.monitoring.controller;

import com.mateuszcholyn.monitoring.service.DummyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    private final DummyService dummyService;

    public DummyController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("randomNumber")
    public ResponseEntity<?> randomNumber() {

        return ResponseEntity.ok(dummyService.randomNumber());
    }

}
