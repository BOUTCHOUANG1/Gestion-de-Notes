package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import lombok.Data;

@Data
public class RevendicationRequest {
    private Exam period;
    private Student student;
    private Grades grade;
    private Semester semester;
    private Double requestedScore;
    private String description;
}
