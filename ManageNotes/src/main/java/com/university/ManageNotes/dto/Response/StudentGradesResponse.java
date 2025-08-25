package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradesResponse {

    private Long studentId;
    private String studentName;
    private Long semesterId;
    private String semesterName;
    private List<GradeResponse> grades;
    private Double gpa;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String username; // matricule for students
    private String level;
    private String role;
    private List<SubjectResponse> subjects;

}
