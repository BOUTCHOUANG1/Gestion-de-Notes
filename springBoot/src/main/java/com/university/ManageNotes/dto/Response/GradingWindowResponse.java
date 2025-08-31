package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.model.PeriodType;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class GradingWindowResponse {
    private Long id;
    private String name;
    private String shortName;
    private PeriodType periodLabel;
    private Integer semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isActive;
    private Integer order;
    private Instant createdDate;
    private Instant lastModifiedDate;
}