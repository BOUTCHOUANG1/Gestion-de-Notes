package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradingWindowRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.service.GradingWindowService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grading-windows")
@RequiredArgsConstructor
public class GradingWindowController {
    private final GradingWindowService gradingWindowService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all grading windows")
    public ResponseEntity<List<GradingWindowResponse>> getAllWindows() {
        return ResponseEntity.ok(gradingWindowService.getAllWindows());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new grading window")
    public ResponseEntity<GradingWindowResponse> createWindow(@Valid @RequestBody GradingWindowRequest request) {
        return ResponseEntity.ok(gradingWindowService.createWindow(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a grading window")
    public ResponseEntity<GradingWindowResponse> updateWindow(@PathVariable Long id, @Valid @RequestBody GradingWindowRequest request) {
        return ResponseEntity.ok(gradingWindowService.updateWindow(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a grading window")
    public ResponseEntity<MessageResponse> deleteWindow(@PathVariable Long id) {
        gradingWindowService.deleteWindow(id);
        return ResponseEntity.ok(MessageResponse.success("Grading window deleted successfully"));
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Initialize default grading windows")
    public ResponseEntity<MessageResponse> initializeDefaultWindows() {
        try {
            // Create default periods matching frontend expectations
            GradingWindowRequest cc1 = new GradingWindowRequest();
            cc1.setSemesterId(1L);
            cc1.setName("Contrôle continu #1");
            cc1.setShortName("CC #1");
            cc1.setType(com.university.ManageNotes.model.GradingWindow.PeriodType.CC);
            cc1.setStartDate(java.time.LocalDate.of(2025, 10, 1));
            cc1.setEndDate(java.time.LocalDate.of(2025, 11, 28));
            cc1.setColor("#FF8A95");
            cc1.setIsActive(false);
            cc1.setOrder(1);
            gradingWindowService.createWindow(cc1);

            GradingWindowRequest sn1 = new GradingWindowRequest();
            sn1.setSemesterId(1L);
            sn1.setName("Session normale #1");
            sn1.setShortName("SN #1");
            sn1.setType(com.university.ManageNotes.model.GradingWindow.PeriodType.SN);
            sn1.setStartDate(java.time.LocalDate.of(2025, 11, 28));
            sn1.setEndDate(java.time.LocalDate.of(2026, 2, 2));
            sn1.setColor("#FFB366");
            sn1.setIsActive(false);
            sn1.setOrder(2);
            gradingWindowService.createWindow(sn1);

            GradingWindowRequest cc2 = new GradingWindowRequest();
            cc2.setSemesterId(2L);
            cc2.setName("Contrôle continu #2");
            cc2.setShortName("CC #2");
            cc2.setType(com.university.ManageNotes.model.GradingWindow.PeriodType.CC);
            cc2.setStartDate(java.time.LocalDate.of(2026, 2, 2));
            cc2.setEndDate(java.time.LocalDate.of(2026, 3, 15));
            cc2.setColor("#B19CD9");
            cc2.setIsActive(false);
            cc2.setOrder(3);
            gradingWindowService.createWindow(cc2);

            GradingWindowRequest sn2 = new GradingWindowRequest();
            sn2.setSemesterId(2L);
            sn2.setName("Session normale #2");
            sn2.setShortName("SN #2");
            sn2.setType(com.university.ManageNotes.model.GradingWindow.PeriodType.SN);
            sn2.setStartDate(java.time.LocalDate.of(2026, 3, 15));
            sn2.setEndDate(java.time.LocalDate.of(2026, 6, 25));
            sn2.setColor("#A8D982");
            sn2.setIsActive(true);
            sn2.setOrder(4);
            gradingWindowService.createWindow(sn2);

            return ResponseEntity.ok(MessageResponse.success("Default grading windows initialized successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.error("Failed to initialize: " + e.getMessage()));
        }
    }
}
