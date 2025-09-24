package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GradeRequest {
    private Long gradeId;
    private Double score;
    private Double maxValue;
    private String comments;
    private Student student;
    private Subject subject;
    private Teacher examiner;
    private Semester semester;
    private AssessmentType exam;
    private List<Revendication> revendication;
}
