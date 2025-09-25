package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;
import com.university.ManageNotes.model.Semester;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    @Override
    public List<SemesterResponse> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SemesterResponse getSemesterById(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));
        return toResponse(semester);
    }

    @Override
    @Transactional
    public SemesterResponse createSemester(SemesterRequest request) {
        Semester semester = new Semester();
        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setActive(request.getActive());

        Semester saved = semesterRepository.save(semester);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public SemesterResponse updateSemester(Long id, SemesterRequest request) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + id));

        semester.setName(request.getName());
        semester.setStartDate(request.getStartDate());
        semester.setEndDate(request.getEndDate());
        semester.setActive(request.getActive());

        Semester updated = semesterRepository.save(semester);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void updateSemesters(List<SemesterRequest> requests) {
        for (SemesterRequest request : requests) {
            if (request.getId() != null) {
                updateSemester(request.getId(), request);
            }
        }
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new RuntimeException("Semester not found with id: " + id);
        }
        semesterRepository.deleteById(id);
    }

    private SemesterResponse toResponse(Semester semester) {
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