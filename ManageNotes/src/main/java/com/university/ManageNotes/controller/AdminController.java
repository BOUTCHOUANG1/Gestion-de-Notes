package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentUpdateRequest;
import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update student information (Admin only)")
    public ResponseEntity<MessageResponse> updateStudent(
            @PathVariable Long id, 
            @Valid @RequestBody StudentUpdateRequest request) {
        UserProfileResponse response = adminService.updateStudent(id, request);
        return ResponseEntity.ok(MessageResponse.success("Student updated successfully"));
    }

    @PutMapping("/subjects/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update subject information (Admin only)")
    public ResponseEntity<MessageResponse> updateSubject(
            @PathVariable Long id, 
            @Valid @RequestBody SubjectRequest request) {
        var response = adminService.updateSubject(id, request);
        return ResponseEntity.ok(MessageResponse.success("Subject updated successfully"));
    }
}