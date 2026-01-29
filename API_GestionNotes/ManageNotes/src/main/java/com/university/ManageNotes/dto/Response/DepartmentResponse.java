package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class DepartmentResponse {
    private Long departmentId;
    private String departmentName;
    private Set<SubjectResponse> departmentSubjects;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
