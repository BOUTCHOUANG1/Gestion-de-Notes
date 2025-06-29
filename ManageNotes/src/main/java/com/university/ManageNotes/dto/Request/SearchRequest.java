package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String query;
    private String searchType; // "STUDENT", "TEACHER", "SUBJECT", "GRADE"

    @Min(value = 0, message = "Page number must be non-negative")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private Integer size = 20;

    private String sortBy = "id";
    private String sortDirection = "ASC"; // "ASC" or "DESC"


}
