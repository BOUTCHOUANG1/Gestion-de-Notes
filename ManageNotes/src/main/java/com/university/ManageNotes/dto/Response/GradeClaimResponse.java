package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.GradeClaim;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GradeClaimResponse {
    private Long id;
    private Long gradeId;
    private Long studentId;
    private String subjectCode;
    private String period;
    private Double currentScore;
    private Double requestedScore;
    private String cause;
    private String description;
    private String status;
    private String teacherComment;
    private LocalDateTime resolvedAt;


}
