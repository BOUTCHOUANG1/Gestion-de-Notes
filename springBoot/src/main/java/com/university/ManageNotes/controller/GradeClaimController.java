package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeClaimDecisionRequest;
import com.university.ManageNotes.dto.Request.GradeClaimRequest;
import com.university.ManageNotes.dto.Response.GradeClaimResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.GradeClaimService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade-claims")
@RequiredArgsConstructor
public class GradeClaimController {
    private final GradeClaimService claimService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Submit a grade claim (student)")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody GradeClaimRequest req) {
        try {
            GradeClaimResponse response = claimService.create(req);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403)
                    .body(MessageResponse
                            .error("Grade claim submission failed: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse
                            .error("Grade claim submission failed: " + ex.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "List grade claims (teacher/admin)")
    public ResponseEntity<?> list(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<GradeClaimResponse> claims = principal.isTeacher()
                    ? claimService.listClaimsForTeacher(principal.getId())
                    : claimService.listAll();
            return ResponseEntity.ok(claims);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "List grade claims for current teacher")
    public ResponseEntity<?> listForTeacher() {
        try {
            return ResponseEntity.ok(claimService.getClaimsForCurrentTeacher());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(MessageResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Approve a grade claim (teacher/admin)")
    public ResponseEntity<?> approve(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        try {
            GradeClaimResponse response = claimService.approve(id, comment);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(MessageResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Reject a grade claim (teacher/admin)")
    public ResponseEntity<?> reject(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        try {
            GradeClaimResponse response = claimService.reject(id, reason);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse
                            .error(ex.getMessage()));
        }
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all pending claims (admin only)")
    public ResponseEntity<?> getPending() {
        try {
            return ResponseEntity.ok(claimService.getPending());
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse
                            .error(ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get claim by ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(claimService.getById(id));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse
                            .error(ex.getMessage()));
        }
    }
}
