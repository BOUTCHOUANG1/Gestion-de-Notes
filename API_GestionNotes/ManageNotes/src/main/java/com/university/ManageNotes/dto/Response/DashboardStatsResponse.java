package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsResponse {
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalSubjects;
    private Long totalDepartments;
    private Long totalGrades;
    private Long totalClaims;
    private Long pendingClaims;
    private Long activeSemesters;
}
