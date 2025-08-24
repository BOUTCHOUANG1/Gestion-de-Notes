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

    @NotBlank
    @Pattern(regexp = "CC_1|CC_2|SN_1|SN_2", message = "period must be CC_1, CC_2, SN_1 or SN_2")
    private PeriodType period;

    @NotBlank
    private String description;
}
