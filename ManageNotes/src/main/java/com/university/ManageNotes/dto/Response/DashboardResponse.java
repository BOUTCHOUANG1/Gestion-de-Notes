package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Long userId;
    private String userRole;
    private String welcomeMessage;

    // Student Dashboard Data
    private Double currentGPA;
    private Integer totalSubjects;
    private Integer completedAssignments;
    private Integer pendingAssignments;
    private List<GradeResponse> recentGrades;

    // Teacher Dashboard Data
    private Integer totalStudents;
    private Integer totalClasses;
    private Integer gradesEntered;
    private List<SubjectResponse> assignedSubjects;

    // Admin Dashboard Data
    private Integer totalUsers;
    private Integer activeStudents;
    private Integer activeTeachers;
    private Map<String, Object> systemStatistics;



}
