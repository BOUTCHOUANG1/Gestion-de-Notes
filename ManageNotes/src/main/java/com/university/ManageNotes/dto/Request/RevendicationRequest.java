package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class RevendicationRequest {
    private ExamPeriod period;
    private Student student;
    private Grades grade;
    private Semester semester;
    private Double requestedScore;
    private String description;
}
