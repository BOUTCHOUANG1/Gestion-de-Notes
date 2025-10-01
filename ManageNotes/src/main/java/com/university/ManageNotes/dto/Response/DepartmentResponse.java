package com.university.ManageNotes.dto.Response;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class DepartmentResponse {
    // Individual department fields for single operations
    private Long departmentId;
    private String departmentName;
    private Set<SubjectResponse> departmentSubjects;
    private Instant createdDate;
    private Instant lastModifiedDate;
    
    // List for paginated operations
    private Set<DepartmentResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
