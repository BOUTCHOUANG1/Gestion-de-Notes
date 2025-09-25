package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.service.RevendicationPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RevendicationPeriodController {
    private final RevendicationPeriodService revendicationPeriodService;

    @GetMapping("/revendication-period")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all grading windows")
    public ResponseEntity<List<RevendicationPeriodResponse>> getAllRevendicationPeriod() {
        return ResponseEntity.ok(revendicationPeriodService.getAllPeriod());
    }

    @PostMapping("/revendication-period/new")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new grading window")
    public ResponseEntity<RevendicationPeriodResponse> createRevendicatioPeriod(@Valid @RequestBody RevendicationPeriodRequest request) {
        return ResponseEntity.ok(revendicationPeriodService.createPeriod(request));
    }

    @PutMapping("/revendication-period/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a grading window")
    public ResponseEntity<RevendicationPeriodResponse> updateRevendicationPeriod(@PathVariable Long id, @Valid @RequestBody RevendicationPeriodRequest request) {
        return ResponseEntity.ok(revendicationPeriodService.updatePeriod(id, request));
    }

    @DeleteMapping("/revendication-period/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a grading window")
    public ResponseEntity<MessageResponse> deleteRevendicationPeriod(@PathVariable Long id) {
        revendicationPeriodService.deletePeriod(id);
        return ResponseEntity.ok(MessageResponse.success("Grading window deleted successfully"));
    }

    @GetMapping("/revendication-period/active")
    @Operation(summary = "Get all isActive grading windows")
    public ResponseEntity<List<RevendicationPeriodResponse>> getActiveRevendicationPeriod() {
        return ResponseEntity.ok(revendicationPeriodService.getActivePeriods());
    }

}
