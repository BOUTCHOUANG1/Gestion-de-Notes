package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Semesters;
import com.university.ManageNotes.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Provides helper operations around semesters (school periods).
 * For User-Story 3 we expose a single method that lazily initialises default periods
 * whenever the database contains none yet.  Default periods are:
 *  • S1  (1 Jan – 30 Jun of current year)
 *  • S2  (1 Jul – 31 Dec of current year)
 * This keeps the back-end stateless for the first boot while still allowing an
 * administrator to later customise / delete / add periods via existing CRUD.
 */
@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public List<Semesters> getSemestersWithDefaults() {
        if (semesterRepository.count() > 0) {
            return semesterRepository.findAll();
        }
        // Build and persist default periods for current calendar year
        int year = LocalDate.now().getYear();
        Semesters s1 = new Semesters();
        s1.setName("S1 " + year);
        s1.setStartDate(LocalDate.of(year, 1, 1));
        s1.setEndDate(LocalDate.of(year, 6, 30));
        s1.setActive(true);

        Semesters s2 = new Semesters();
        s2.setName("S2 " + year);
        s2.setStartDate(LocalDate.of(year, 7, 1));
        s2.setEndDate(LocalDate.of(year, 12, 31));
        s2.setActive(false);

        List<Semesters> defaults = Arrays.asList(s1, s2);
        semesterRepository.saveAll(defaults);
        return defaults;
    }
}
