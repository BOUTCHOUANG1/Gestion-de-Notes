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

    /**
     * Update multiple semesters at once (name, dates, activation flag, ordering).
     * Functional style: we keep the pipeline immutable – each DB entity is first fetched, then a new instance
     * is created via mapping, finally we persist everything in one call.
     *
     * @param requests list of changes coming from the client
     * @return updated semesters persisted in the DB
     */
    public List<Semesters> updateSemesters(java.util.List<com.university.ManageNotes.dto.Request.SemesterUpdateRequest> requests) {
        // 1. Pre-fetch all existing semesters referenced by the request ids.
        java.util.Map<Long, Semesters> existingById = semesterRepository.findAllById(
                        requests.stream().map(com.university.ManageNotes.dto.Request.SemesterUpdateRequest::getId).toList())
                .stream()
                .collect(java.util.stream.Collectors.toMap(Semesters::getId, java.util.function.Function.identity()));

        // 2. Map each request -> updated entity (copy-on-write) keeping mutation localised.
        java.util.List<Semesters> toSave = requests.stream()
                .map(req -> {
                    Semesters src = existingById.get(req.getId());
                    if (src == null) {
                        throw new IllegalArgumentException("Semester with id %d not found".formatted(req.getId()));
                    }
                    // We mutate the managed entity instance – JPA will detect changes. still functional enough.
                    if (req.getName() != null) src.setName(req.getName());
                    if (req.getStartDate() != null) src.setStartDate(req.getStartDate());
                    if (req.getEndDate() != null) src.setEndDate(req.getEndDate());
                    if (req.getActive() != null) src.setActive(req.getActive());
                    if (req.getOrderIndex() != null) src.setOrderIndex(req.getOrderIndex());
                    return src;
                })
                .toList();

        // 3. Persist in one batch – reduces round-trips.
        return semesterRepository.saveAll(toSave);
    }
}
