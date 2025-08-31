package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradingWindowRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.model.PeriodType;
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

    @GetMapping("/active")
    @Operation(summary = "Get all active grading windows")
    public ResponseEntity<List<GradingWindowResponse>> getActiveWindows() {
        return ResponseEntity.ok(gradingWindowService.getActiveWindows());
    }

}
