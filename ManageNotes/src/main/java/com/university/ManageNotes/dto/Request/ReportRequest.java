package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ReportRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;

    private String reportType; // "TRANSCRIPT", "GRADE_SUMMARY", "DETAILED"
    private String format; // "PDF", "EXCEL"
    private List<Long> subjectIds; // Optional: specific subjects only
    private Boolean includeComments = true;
    private Boolean includePdf = false;
    private String recipientEmail; // For email delivery

    // new metadata filled by teacher/admin
    private String faculty;
    private String universityName;
    private String academicYear;

}
