package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.GradeType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeByCodeRequest {
    @NotBlank
    private String studentMatricule;

    @NotBlank
    private String subjectCode;

    @NotNull
    private Long semesterId;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double value;

    @NotNull
    private GradeType type;

    private String comments;

    private String periodLabel;
}
