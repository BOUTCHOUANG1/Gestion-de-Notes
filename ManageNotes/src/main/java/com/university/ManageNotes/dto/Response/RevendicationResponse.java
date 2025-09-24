package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevendicationResponse {
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
