package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import com.university.ManageNotes.model.GradeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class GradeResponse extends AbstractEntity {
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long semesterId;
    private String semesterName;
    private Double value;
    private GradeType type;
    private String periodLabel;
    private String comments;
    private Long enteredBy;
    private String enteredByName;
    private boolean passed;
    private BigDecimal creditsEarned;
}
