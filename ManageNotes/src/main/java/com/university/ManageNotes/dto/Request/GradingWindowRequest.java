package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.GradingWindow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GradingWindowRequest {
    @NotNull
    private Long semesterId;

    @NotBlank
    private String name;

    @NotBlank
    private String shortName;

    @NotNull
    private GradingWindow.PeriodType type;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String color;
    private Boolean isActive = false;
    private Integer order;
}
