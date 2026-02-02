package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.DashboardStatsResponse;
import com.university.ManageNotes.dto.Response.RecentActivityResponse;
import com.university.ManageNotes.dto.Response.StudentsByLevelResponse;
import com.university.ManageNotes.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Admin dashboard statistics and analytics")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;
    private final RevendicationRepository revendicationRepository;
    private final SemesterRepository semesterRepository;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve comprehensive dashboard statistics")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = DashboardStatsResponse.builder()
                .totalStudents(studentRepository.count())
                .totalTeachers(teacherRepository.count())
                .totalSubjects(subjectRepository.count())
                .totalDepartments(departmentRepository.count())
                .totalGrades(gradeRepository.count())
                .totalClaims(revendicationRepository.count())
                .pendingClaims(revendicationRepository.countByStatus("PENDING"))
                .activeSemesters(semesterRepository.countByActiveTrue())
                .build();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/students-by-level")
    @Operation(summary = "Get students distribution by level")
    public ResponseEntity<List<StudentsByLevelResponse>> getStudentsByLevel() {
        List<Object[]> results = studentRepository.countByLevel();
        
        List<StudentsByLevelResponse> response = results.stream()
                .map(result -> StudentsByLevelResponse.builder()
                        .level((String) result[0])
                        .count((Long) result[1])
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent-activity")
    @Operation(summary = "Get recent system activity")
    public ResponseEntity<List<RecentActivityResponse>> getRecentActivity(
            @RequestParam(defaultValue = "10") int limit) {
        
        // For now, return empty list - can be enhanced with actual activity tracking
        List<RecentActivityResponse> activities = List.of(
            RecentActivityResponse.builder()
                .id(1L)
                .type("grade")
                .description("New grade added for Mathematics")
                .timestamp(java.time.LocalDateTime.now().minusHours(1))
                .user("Prof. Johnson")
                .build(),
            RecentActivityResponse.builder()
                .id(2L)
                .type("claim")
                .description("Grade claim submitted for Physics")
                .timestamp(java.time.LocalDateTime.now().minusHours(2))
                .user("STU2024001")
                .build()
        );

        return ResponseEntity.ok(activities.stream().limit(limit).collect(Collectors.toList()));
    }
}
