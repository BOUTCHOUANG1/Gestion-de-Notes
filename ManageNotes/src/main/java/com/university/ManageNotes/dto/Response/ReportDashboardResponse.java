package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportDashboardResponse {
    private String subjectCode;
    private String subjectName;
    private String level;
    private String cycle;
    private Long semesterId;
    private String semesterName;

    private String period;

    private int studentCount;
    private int conflictCount;

    private List<String> columns;
    private List<Row> rows;

    @Data
    public static class Row {
        private String matricule;
        private String name;
        private Map<String, Double> grades;
        private boolean passed;
        private int creditsEarned;
    }
}
