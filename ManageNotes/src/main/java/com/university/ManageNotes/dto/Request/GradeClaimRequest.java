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
    @Pattern(regexp = "CC|SN", message = "period must be CC or SN")
    private String period;

    @NotBlank
    private String description;
}
