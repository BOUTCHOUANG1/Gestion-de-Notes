package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.SubjectPerformance;
import com.university.ManageNotes.model.TopPerformer;
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
public class StatisticsResponse {

    private Long semesterId;
    private String semesterName;

    // Grade Statistics
    private Double averageGPA;
    private Integer totalGrades;
    private Map<String, Integer> gradeDistribution; // "A": 25, "B": 30, etc.
    private Map<String, Double> subjectAverages;

    // Performance Statistics
    private Integer passRate;
    private Integer failRate;
    private List<TopPerformer> topPerformers;
    private List<SubjectPerformance> subjectPerformances;

}
