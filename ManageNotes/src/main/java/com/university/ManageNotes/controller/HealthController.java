package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "System health endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check endpoint")
    public HealthCheckResponse health() {
        HealthCheckResponse response = new HealthCheckResponse();
        response.setStatus("UP");
        response.setMessage("ManageNotes API is running");
        response.setTimestamp(java.time.Instant.now());
        return response;
    }
}