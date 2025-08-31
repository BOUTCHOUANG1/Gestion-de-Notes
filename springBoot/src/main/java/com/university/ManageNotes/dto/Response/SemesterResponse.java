package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterResponse {
    
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Instant createdDate;
    private Instant lastModifiedDate;
}