package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    @NotBlank(message = "Subject name is required")
    @Size(min = 2, max = 100, message = "Subject name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Subject code is required")
    @Size(min = 3, max = 20, message = "Subject code must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Subject code must contain only uppercase letters and numbers")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 10, message = "Credits must not exceed 10")
    private Integer credits = 1;

    private Long teacherId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Level is required")
    private StudentLevel level;

    @NotNull(message = "Cycle is required")
    private StudentCycle cycle;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;

    private Boolean active = true;

}
