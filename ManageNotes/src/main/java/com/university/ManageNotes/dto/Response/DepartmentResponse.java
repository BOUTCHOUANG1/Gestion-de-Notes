package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.model.Subject;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class DepartmentResponse {
    private Long departmentId;
    private String departmentName;

    private List<DepartmentRequest> content;
    private Set<SubjectResponse> subjects;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
