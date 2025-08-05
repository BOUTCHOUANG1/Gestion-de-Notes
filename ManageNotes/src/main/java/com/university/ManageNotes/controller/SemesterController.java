package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Semesters;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/semesters")
@Tag(name = "Semester Management", description = "Create and list semesters (Admin only)")
public class SemesterController {

    private final SemesterRepository semesterRepository;

    private final SemesterService semesterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new semester", description = "Admin only")
    public ResponseEntity<?> createSemester(@Valid @RequestBody SemesterRequest request) {
        if (semesterRepository.existsByName(request.getName())) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Semester name already exists"));
        }
        Semesters semester = new Semesters();
        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setActive(Boolean.TRUE.equals(request.getActive()));
        Semesters saved = semesterRepository.save(semester);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a semester", description = "Admin only")
    public com.university.ManageNotes.dto.Response.MessageResponse deleteSemester(@PathVariable Long id) {
        if (!semesterRepository.existsById(id)) {
            return com.university.ManageNotes.dto.Response.MessageResponse.error("Semester not found");
        }
        semesterRepository.deleteById(id);
        return com.university.ManageNotes.dto.Response.MessageResponse.success("Semester deleted successfully");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all semesters, creating defaults if none exist")
    public List<Semesters> listSemesters() {
        return semesterService.getSemestersWithDefaults();
    }

    /**
     * Bulk update of school periods – covers user-story N4 AC2+AC3.
     * We accept a list so the admin can edit names, dates, ordering in one shot.
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bulk update semesters", description = "Update name, dates, active flag and order index for many semesters at once")
    public List<Semesters> updateSemesters(@Valid @RequestBody java.util.List<com.university.ManageNotes.dto.Request.SemesterUpdateRequest> requests) {
        return semesterService.updateSemesters(requests);
    }
}
