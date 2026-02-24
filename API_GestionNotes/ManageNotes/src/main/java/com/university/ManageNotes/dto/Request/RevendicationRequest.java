package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RevendicationRequest {
    @NotNull(message = "Grade ID is required")
    private Long gradeId;

    @NotNull(message = "Exam period ID is required")
    private Long examPeriodId;

    @NotNull(message = "Requested score is required")
    @DecimalMin(value = "0.0", message = "Requested score must be positive")
    @DecimalMax(value = "20.0", message = "Requested score cannot exceed 20")
    private Double requestedScore;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
}
