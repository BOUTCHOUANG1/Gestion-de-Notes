package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.PeriodType;
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

    @NotNull
    private PeriodType period;

    @NotBlank
    private String description;
}
