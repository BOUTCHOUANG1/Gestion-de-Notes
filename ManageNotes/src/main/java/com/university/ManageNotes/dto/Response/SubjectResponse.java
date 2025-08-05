package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private BigDecimal credits;
    private String description;
    private Boolean active;
    private com.university.ManageNotes.model.StudentLevel level;
    private com.university.ManageNotes.model.StudentCycle cycle;
    private Long semesterId;
    private String semesterName;
}
