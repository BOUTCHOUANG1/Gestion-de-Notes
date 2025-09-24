package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RevendicationRequest {
    @NotNull
    private Long gradeId;

    @NotNull
    private Double requestedScore;

    @NotBlank
    private String cause;

    @NotNull
    private ExamPeriod period;

    @NotBlank
    private String description;
}
