package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GradingWindowRequest {
    @NotNull
    private Long semesterId;

    @NotBlank
    private String name; // CC or SN

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active = true;
}
