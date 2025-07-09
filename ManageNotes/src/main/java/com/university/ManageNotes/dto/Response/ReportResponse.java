package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse extends AbstractEntity {

    private Long studentId;
    private String studentName;
    private String level;
    private Long semesterId;
    private String semesterName;
    private Double gpa;
    private Double annualAverage;
    private String status; // "PASS", "FAIL", "INCOMPLETE"
    private String pdfPath;
    private Integer creditsEarned;

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

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
