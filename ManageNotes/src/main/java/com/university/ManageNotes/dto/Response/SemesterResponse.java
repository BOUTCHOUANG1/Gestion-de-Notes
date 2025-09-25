package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Revendication;
import com.university.ManageNotes.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterResponse {
    private Long semesterId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private List<Revendication> revendications;
    private List<Subject> subjects;
    private List<Grades> grades;
}