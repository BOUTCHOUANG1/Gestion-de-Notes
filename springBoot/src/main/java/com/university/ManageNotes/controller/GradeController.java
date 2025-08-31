package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade Management", description = "Grade management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
public class GradeController {


    private final GradeService gradeService;


    private final StudentRepository studentRepository;

    @PostMapping
    @Operation(summary = "Create new grade", description = "Create a new grade entry (Teacher/Admin only)")
    public ResponseEntity<?> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
        try {
            GradeResponse response = gradeService.createGrade(gradeRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error creating grade: " + e.getMessage(), "ERROR"));
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
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating grade: " + e.getMessage(), "ERROR"));
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
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Get teacher's grades", description = "Get all grades entered by current teacher")
    public ResponseEntity<?> getTeacherGrades() {
        try {
            List<GradeResponse> response = gradeService.getTeacherGrades();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error fetching grades: " + e.getMessage(), "ERROR"));
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
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error generating grade sheet: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/semester-summary")
    @Operation(summary = "Get semester summary", description = "Get a summary of a student's performance for a semester")
    public ResponseEntity<?> getSemesterSummary(
            @RequestParam Long studentId,
            @RequestParam Long semesterId) {
        try {
            ReportResponse response = gradeService.calculateSemesterSummary(studentId, semesterId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error calculating semester summary: " + e.getMessage(), "ERROR"));
        }
    }
    
    @GetMapping("/year-summary")
    @Operation(summary = "Get year summary", description = "Get promotion status based on both semesters (>= 55 credits, GPA > 2.0)")
    public ResponseEntity<?> getYearSummary(
            @RequestParam Long studentId,
            @RequestParam Long semester1Id,
            @RequestParam Long semester2Id) {
        try {
            ReportResponse response = gradeService.calculateYearSummary(studentId, semester1Id, semester2Id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error calculating year summary: " + e.getMessage(), "ERROR"));
        }
    }

    @GetMapping("/sheet/self")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getMySheet(@AuthenticationPrincipal UserPrincipal principal,
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

    @GetMapping("/level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get grades for all students in a level (Admin only)")
    public ResponseEntity<?> getGradesByLevel(@PathVariable String level) {
        try {
            var lvlOpt = Arrays.stream(StudentLevel.values())
                    .filter(l -> l.name().equals("LEVEL" + level.replace("L", "")))
                    .findFirst();
            if (lvlOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid level", "ERROR"));
            }
            var students = studentRepository.findByLevel(lvlOpt.get());
            var results = students.stream()
                    .map(s -> gradeService.getStudentGrades(s.getId(), null))
                    .toList();
            return ResponseEntity.ok(results);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error fetching grades: " + ex.getMessage(), "ERROR"));
        }
    }
}
