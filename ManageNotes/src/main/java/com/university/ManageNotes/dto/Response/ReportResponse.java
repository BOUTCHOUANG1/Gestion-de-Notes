package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse extends AbstractEntity {

    private Long studentId;
    private String studentName;
    private Long semesterId;
    private String semesterName;
    private Double gpa;
    private String status; // "PASS", "FAIL", "INCOMPLETE"
    private String pdfPath;

    private Long generatedBy;
    private String generatedByName;
    private String downloadUrl;


}
