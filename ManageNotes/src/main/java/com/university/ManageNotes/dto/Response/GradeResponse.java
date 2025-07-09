package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import com.university.ManageNotes.model.GradeType;
import lombok.Data;

@Data
public class GradeResponse extends AbstractEntity {
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long semesterId;
    private String semesterName;
    private Double value;
    private Double coefficient;
    private GradeType type;
    private String comments;
    private Long enteredBy;
    private String enteredByName;
}
