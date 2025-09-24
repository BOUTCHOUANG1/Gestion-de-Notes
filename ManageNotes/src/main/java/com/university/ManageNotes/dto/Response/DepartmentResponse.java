package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import lombok.Data;
import java.util.List;

@Data
public class DepartmentResponse {
    private List<DepartmentRequest> content;
    private List<SubjectResponse> subjects;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
