package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class GradeResponse {
    private Long gradeId;
    private Double score;
    private Double maxValue;
    private String comments;
    private Student student;
    private Subject subject;
    private Teacher examiner;
    private Semester semester;
    private AssessmentType exam;
    private List<Revendication> revendication;
    private Boolean hasPassed;
    private Double gpa;
    private GradeRequest content;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
