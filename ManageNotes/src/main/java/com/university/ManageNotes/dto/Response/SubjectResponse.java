package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private List<SubjectRequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
