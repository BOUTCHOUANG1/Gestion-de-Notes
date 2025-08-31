package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.util.Map;

@Data
public class GradeSheetRow {
    private Long studentId;
    private String matricule;
    private String fullName;
    // map gradeType -> value
    private Map<String, Double> grades;
    private boolean passed;
}
