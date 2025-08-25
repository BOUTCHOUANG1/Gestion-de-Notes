package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor

public class SemesterRequest {

    @NotBlank(message = "Semester name is required")
    @Size(min = 3, max = 50, message = "Semester name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Boolean active = false;


}
