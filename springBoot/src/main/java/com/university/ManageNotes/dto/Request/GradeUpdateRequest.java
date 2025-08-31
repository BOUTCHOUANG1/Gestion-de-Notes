package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.GradeType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GradeUpdateRequest {
    @DecimalMin(value = "0.0", message = "Grade value must be at least 0")
    @DecimalMax(value = "20.0", message = "Grade value must not exceed 20")
    private Double value;

    private GradeType type;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;
}
