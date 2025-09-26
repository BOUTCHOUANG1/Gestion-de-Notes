package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentCycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    private String subjectCode;
    private BigDecimal credits;
    private String description;
    private Long teacherId;
    private List<TeachingLevel> subjectsLevel;
    private StudentCycle Studentcycle;
    private Long semesterId;
    private Long departmentId;
}
