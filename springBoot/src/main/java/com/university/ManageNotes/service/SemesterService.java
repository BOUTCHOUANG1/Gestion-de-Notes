package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;
import com.university.ManageNotes.model.Semesters;
import com.university.ManageNotes.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public List<SemesterResponse> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SemesterResponse getSemesterById(Long id) {
        Semesters semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        return toResponse(semester);
    }

    @Transactional
    public SemesterResponse createSemester(SemesterRequest request) {
        Semesters semester = new Semesters();
        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setActive(request.getActive());

        Semesters saved = semesterRepository.save(semester);
        return toResponse(saved);
    }

    @Transactional
    public SemesterResponse updateSemester(Long id, SemesterRequest request) {
        Semesters semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));

        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setActive(request.getActive());

        Semesters updated = semesterRepository.save(semester);
        return toResponse(updated);
    }

    @Transactional
    public void updateSemesters(List<SemesterRequest> requests) {
        for (SemesterRequest request : requests) {
            if (request.getId() != null) {
                updateSemester(request.getId(), request);
            }
        }
    }

    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new RuntimeException("Semester not found with id: " + id);
        }
        semesterRepository.deleteById(id);
    }

    private SemesterResponse toResponse(Semesters semester) {
        return SemesterResponse.builder()
                .id(semester.getId())
                .name(semester.getName())
                .startDate(semester.getStartDate())
                .endDate(semester.getEndDate())
                .active(semester.getActive())
                .createdDate(semester.getCreatedDate())
                .lastModifiedDate(semester.getLastModifiedDate())
                .build();
    }
}