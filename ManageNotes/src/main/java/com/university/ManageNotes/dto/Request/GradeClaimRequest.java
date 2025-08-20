package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeClaimRequest {
    @NotNull
    private Long gradeId;

    @NotNull
    private Double requestedScore;

    @NotBlank
    private String cause;

    @NotBlank
    private String description;
}
