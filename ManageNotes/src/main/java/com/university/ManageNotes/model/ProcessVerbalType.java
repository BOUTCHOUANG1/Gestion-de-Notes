package com.university.ManageNotes.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enumeration of printable "procès-verbal" types requested in User-Story N10.
 */
public enum ProcessVerbalType {
    STUDENT_LIST,
    GRADE_CC1,
    GRADE_SN1,
    GRADE_CC2,
    GRADE_SN2,
    YEAR_SUMMARY;

    public static Optional<ProcessVerbalType> fromLabel(String label) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(label.replace(" ", "_")))
                .findFirst();
    }
}
