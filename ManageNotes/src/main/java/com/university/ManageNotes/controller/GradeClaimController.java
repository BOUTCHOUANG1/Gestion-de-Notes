package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeClaimDecisionRequest;
import com.university.ManageNotes.dto.Request.GradeClaimRequest;
import com.university.ManageNotes.dto.Response.GradeClaimResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.GradeClaim;
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
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal principal,
                                    @Valid @RequestBody GradeClaimRequest req) {
        try {
            GradeClaim claim = claimService.createClaim(principal.getId(), req);
            return ResponseEntity.ok(GradeClaimResponse.fromEntity(claim));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(403).body(MessageResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(MessageResponse.error(ex.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "List grade claims (teacher/admin)")
    public ResponseEntity<?> list(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<GradeClaim> claims = principal.isTeacher()
                    ? claimService.listClaimsForTeacher(principal.getId())
                    : claimService.listAll();
            return ResponseEntity.ok(claims.stream().map(GradeClaimResponse::fromEntity).toList());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(MessageResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Decide on a grade claim (approve/reject)")
    public ResponseEntity<?> decide(@PathVariable Long id,
                                    @Valid @RequestBody GradeClaimDecisionRequest decision) {
        try {
            GradeClaim updated = claimService.decide(id, decision.getApprove(), decision.getComment());
            return ResponseEntity.ok(GradeClaimResponse.fromEntity(updated));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(MessageResponse.error(ex.getMessage()));
        }
    }
}
