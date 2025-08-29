package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Subject Lookup", description = "Endpoints for subject lookup")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    @Operation(summary = "Get all subjects")
    public List<SubjectResponse> all() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by id")
    public SubjectResponse one(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create subject", description = "Teacher or Admin can create a subject")
    public MessageResponse create(@Valid @RequestBody SubjectRequest request) {
        return subjectService.createSubject(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update subject", description = "Teacher or Admin can update a subject")
    public MessageResponse update(@PathVariable Long id,
                                  @Valid @RequestBody SubjectRequest request) {
        return subjectService.updateSubject(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete subject",
            description = "Teacher or Admin can delete a subject")
    public MessageResponse delete(@PathVariable Long id) {
        return subjectService.deleteSubject(id);
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get subjects assigned to current teacher",
            description = "Return subjects taught by the authenticated teacher")
    public List<SubjectResponse> assignedSubjects(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long teacherId = userPrincipal.getId();
        return subjectService.getSubjectsByTeacher(teacherId);
    }

    /**
     * View-only endpoint for administrators to retrieve the list of subjects.
     * <p>
     * Functional-style implementation: the service call returns a List which we convert to an Optional.
     * If the list is empty, we map it to an internationalised fallback message; otherwise we return the list itself.
     * This avoids imperative <code>if/else</code> logic and relies on the monadic map capabilities of {@link java.util.Optional}.
     * </p>
     *
     * @return 200-OK with either the subject list or a fallback message.
     */
    @GetMapping("/view-only")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listViewOnly() {
        var subjects = subjectService.getAllSubjects();
        return Optional.of(subjects)
                .filter(list -> !list.isEmpty())
                .<org.springframework.http.ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok("No subjects found"));
    }
}
