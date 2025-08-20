package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
