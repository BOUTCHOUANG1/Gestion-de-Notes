package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
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

}
