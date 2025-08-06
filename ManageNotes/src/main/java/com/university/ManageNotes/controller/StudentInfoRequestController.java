package com.university.ManageNotes.controller;

// ... existing code ... <imports>
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.StudentInfoRequest;
import com.university.ManageNotes.service.StudentInfoRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/info-requests")
@RequiredArgsConstructor
@Tag(name = "Student Info Requests", description = "Endpoints to manage student info modification requests")
public class StudentInfoRequestController {

    private final StudentInfoRequestService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List pending requests")
    public List<StudentInfoRequest> listPending() {
        return service.getPendingRequests();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get request details")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return service.getRequest(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse approve(@PathVariable Long id) {
        return service.approve(id);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse reject(@PathVariable Long id, @RequestParam String reason) {
        return service.reject(id, reason);
    }
}
