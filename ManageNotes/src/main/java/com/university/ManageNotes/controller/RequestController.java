// controller/RequestController.java
package com.university.ManageNotes.controller;

import com.university.ManageNotes.service.GradeClaimService;
import com.university.ManageNotes.service.StudentInfoRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final GradeClaimService gradeClaimService;
    private final StudentInfoRequestService studentInfoRequestService;

    @PostMapping("/grade-claims")
    public ResponseEntity<?> createGradeClaim(@RequestBody com.university.ManageNotes.dto.Request.GradeClaimRequest request) {
        return ResponseEntity.ok(gradeClaimService.create(request));
    }

    @PostMapping("/grade-claims/{id}/approve")
    public ResponseEntity<?> approveGradeClaim(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(gradeClaimService.approve(id, comment));
    }

    @PostMapping("/grade-claims/{id}/reject")
    public ResponseEntity<?> rejectGradeClaim(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(gradeClaimService.reject(id, reason));
    }

    @GetMapping("/grade-claims/pending")
    public ResponseEntity<?> getPendingGradeClaims() {
        return ResponseEntity.ok(gradeClaimService.getPending());
    }

    @GetMapping("/grade-claims/{id}")
    public ResponseEntity<?> getGradeClaim(@PathVariable Long id) {
        return ResponseEntity.ok(gradeClaimService.getById(id));
    }

    // Similar endpoints for student info requests...
}
