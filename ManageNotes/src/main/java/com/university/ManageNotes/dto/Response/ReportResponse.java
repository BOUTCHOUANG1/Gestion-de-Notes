package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportResponse extends AbstractEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectResult {
        private String subjectName;
        private double average;
        private BigDecimal credits;
        private boolean passed;
    }
    
    private List<SubjectResult> subjectResults;

    private Long studentId;
    private String studentName;
    private String level;
    private Long semesterId;
    private String semesterName;
    private Double gpa;
    private Double annualAverage;
    private String status; // "PASS", "FAIL", "PROMOTED", "NOT_PROMOTED"
    private String pdfPath;
    private Integer creditsEarned;
    
    // University promotion fields
    private Integer totalCreditsRequired; // 60 for academic year
    private Integer semester1Credits;
    private Integer semester2Credits;
    private Double semester1Average;
    private Double semester2Average;
    private Boolean promotionEligible;
    private String promotionReason; // Why promoted/not promoted

    // new fields for header info
    private String faculty;
    private String universityName;
    private String academicYear;

    // Detailed grade information for the report
    private List<GradeResponse> grades;

    private Long generatedBy;
    private String generatedByName;
    private String downloadUrl;

    // Additional fields for different report types
    private String reportType;
    private Long subjectId;
    private String subjectName;
    private Boolean success;
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
