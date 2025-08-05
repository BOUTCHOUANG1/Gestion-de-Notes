package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.GradeSheetResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade Management", description = "Grade management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private com.university.ManageNotes.repository.StudentRepository studentRepository;

    @PostMapping
    @Operation(summary = "Create new grade", description = "Create a new grade entry (Teacher/Admin only)")
    public ResponseEntity<?> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
        try {
            GradeResponse response = gradeService.createGrade(gradeRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error creating grade: " + e.getMessage(), "ERROR"));
        }
    }

    @PostMapping("/by-code")
    @Operation(summary = "Create grade by matricule & subject code", description = "Teacher enters grade using student matricule and subject code")
    public ResponseEntity<?> createGradeByCode(@Valid @RequestBody com.university.ManageNotes.dto.Request.GradeByCodeRequest request) {
        try {
            GradeResponse resp = gradeService.createGradeByCode(request);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error creating grade: " + e.getMessage(), "ERROR"));
        }
    }

    @PutMapping("/{gradeId}")
    @Operation(summary = "Update grade", description = "Update an existing grade (Teacher/Admin only)")
    public ResponseEntity<?> updateGrade(
            @PathVariable Long gradeId,
            @Valid @RequestBody GradeUpdateRequest updateRequest) {
        try {
            GradeResponse response = gradeService.updateGrade(gradeId, updateRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error updating grade: " + e.getMessage(), "ERROR"));
        }
    }

    @DeleteMapping("/{gradeId}")
    @Operation(summary = "Delete grade", description = "Delete a grade entry (Teacher/Admin only)")
    public ResponseEntity<MessageResponse> deleteGrade(@PathVariable Long gradeId) {
        try {
            gradeService.deleteGrade(gradeId);
            return ResponseEntity.ok(new MessageResponse("Grade deleted successfully", "SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error deleting grade: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get student grades", description = "Get all grades for a specific student")
    public ResponseEntity<?> getStudentGrades(
            @PathVariable Long studentId,
            @RequestParam(required = false) Long semesterId) {
        try {
            StudentGradesResponse response = gradeService.getStudentGrades(studentId, semesterId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error fetching grades: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/teacher/my-grades")
    @Operation(summary = "Get teacher's grades", description = "Get all grades entered by current teacher")
    public ResponseEntity<?> getTeacherGrades() {
        try {
            List<GradeResponse> response = gradeService.getTeacherGrades();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error fetching grades: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/sheet")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Get grade sheet", description = "Return the full grade sheet for a subject and semester. Teachers may only request their own subject.")
    public ResponseEntity<?> getGradeSheet(@RequestParam String subjectCode,
                                           @RequestParam Long semesterId,
                                           @RequestParam(required = false) String period) {
        try {
            GradeSheetResponse resp = gradeService.getGradeSheet(subjectCode, semesterId, period);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error generating grade sheet: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/sheet/self")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMySheet(@AuthenticationPrincipal com.university.ManageNotes.security.UserPrincipal principal,
                                        @RequestParam Long semesterId) {
        var studentOpt = studentRepository.findByMatricule(principal.getUsername());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Student record not found", "ERROR"));
        }
        var student = studentOpt.get();
        // We need subjectCode – show all subjects ? For now return all grades for the semester aggregated
        var rows = gradeService.getStudentGrades(student.getId(), semesterId);
        return ResponseEntity.ok(rows);
    }
}
