package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.ExamPeriod;
import com.university.ManageNotes.model.Semester;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RevendicationPeriodRequest {
    private Long revendicationPeriodId;
    private ExamPeriod examPeriod;
    private Semester semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isActive = false;
}
