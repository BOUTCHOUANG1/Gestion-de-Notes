package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.service.DepartmentService;
import com.university.ManageNotes.util.ResponseMapper;
import com.university.ManageNotes.mapper.DepartmentMapper;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import java.util.HashSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentMapper departmentMapper;
    private final ResponseMapper responseMapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        // Map DTO to Entity
        Department department = departmentMapper.toEntity(request);

        // Check if department name already exists
        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new APIException("Department with name '" + department.getDepartmentName() + "' already exists");
        }

        department.setCreatedDate(Instant.now());
        department.setLastModifiedDate(Instant.now());

        // Handle subjects if provided
        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            Set<Subject> subjects = new HashSet<>();
            for (Long subjectId : request.getSubjectIds()) {
                Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));
                subject.setDepartment(department);
                subjects.add(subject);
            }
            department.setSubjects(subjects);
        }

        // Save and map back to DTO
        Department savedDepartment = departmentRepository.save(department);
        return mapToDepartmentResponse(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase(AppConstant.SORT_DIR) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        List<Department> departments = departmentPage.getContent();
        if (departments.isEmpty()) {
            throw new APIException("No departments found");
        }

        return departments.stream()
                .map(this::mapToDepartmentResponse)
                .collect(Collectors.toList());
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        // Manually load subjects
        java.util.List<Subject> subjects = subjectRepository.findByDepartment(department);
        department.setSubjects(new HashSet<>(subjects));
        return responseMapper.toDepartmentResponse(department);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(DepartmentRequest request, Long departmentId) {
        // Map DTO to Entity
        Department departmentUpdate = departmentMapper.toEntity(request);
        
        Department departmentDb = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        // Update department name
        if (departmentUpdate.getDepartmentName() != null) {
            // Check if new name already exists (excluding current department)
            if (departmentRepository.existsByDepartmentNameAndDepartmentIdNot(
                    departmentUpdate.getDepartmentName(), departmentId)) {
                throw new APIException("Department with name '" + departmentUpdate.getDepartmentName() + "' already exists");
            }
            departmentDb.setDepartmentName(departmentUpdate.getDepartmentName());
        }

        // Update subjects if provided
        if (request.getSubjectIds() != null) {
            // Clear existing subjects
            departmentDb.getSubjects().forEach(subject ->
                    subject.setDepartment(null));
            departmentDb.getSubjects().clear();

            // Add new subjects
            if (!request.getSubjectIds().isEmpty()) {
                Set<Subject> newSubjects = new HashSet<>();
                for (Long subjectId : request.getSubjectIds()) {
                    Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));
                    subject.setDepartment(departmentDb);
                    newSubjects.add(subject);
                }
                departmentDb.setSubjects(newSubjects);
            }
        }

        departmentDb.setLastModifiedDate(Instant.now());

        // Save and map back to DTO
        Department savedDepartment = departmentRepository.save(departmentDb);
        return mapToDepartmentResponse(savedDepartment);
    }

    @Override
    @Transactional
    public DepartmentResponse deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        // Remove department reference from subjects before deletion
        department.getSubjects().forEach(subject
                -> subject.setDepartment(null));
        
        // Map to DTO before deletion
        DepartmentResponse deletedDepartment = mapToDepartmentResponse(department);
        departmentRepository.delete(department);
        
        return deletedDepartment;
    }
}