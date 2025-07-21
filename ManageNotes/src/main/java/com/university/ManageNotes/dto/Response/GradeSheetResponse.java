package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.util.List;

@Data
public class GradeSheetResponse {
    private String subjectCode;
    private String subjectName;
    private String level;
    private String cycle;
    private String period;
    private int totalStudents;
    private int conflicts;

    private List<GradeSheetColumn> columns;
    private List<GradeSheetRow> rows;
}
