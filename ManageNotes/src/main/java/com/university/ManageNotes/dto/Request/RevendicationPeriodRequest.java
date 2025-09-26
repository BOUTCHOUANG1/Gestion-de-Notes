package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Exam;
import com.university.ManageNotes.model.Semester;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RevendicationPeriodRequest {
    private Long revendicationPeriodId;
    private Exam exam;
    private Semester semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isActive = false;
}
