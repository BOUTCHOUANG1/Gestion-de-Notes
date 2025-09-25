package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
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
    private Teacher teacher;
    private List<TeachingLevel> subjectsLevel;
    private StudentCycle Studentcycle;
    private Semester semester;
    private Department department;
}
