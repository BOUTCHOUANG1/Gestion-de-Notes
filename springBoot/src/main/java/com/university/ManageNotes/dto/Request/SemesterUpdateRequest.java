package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request payload for updating an existing semester.
 * Immutable data-carrier that plays nicely with functional style (no mutation after construction).
 */
@Getter
@Setter
public class SemesterUpdateRequest {
    @NotNull(message = "Semester id is required")
    private Long id;

    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private Boolean active;

    /**
     * New ordering index for the semester.  May be null – in that case the ordering isn't changed.
     */
    private Integer orderIndex;
}
