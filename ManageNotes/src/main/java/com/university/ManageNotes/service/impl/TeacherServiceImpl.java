package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.repository.TeacherRepository;
import com.university.ManageNotes.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    private final ModelMapper modelMapper;

    @Override
    public TeacherRequest updateTeacher(Long teacherId, TeacherRequest request) {
        Teacher teacher = modelMapper.map(request, Teacher.class);

        Teacher teacherFromDb = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        // Update Teacher entity
        teacherFromDb.setFirstName(teacher.getFirstName());
        teacherFromDb.setLastName(teacher.getLastName());
        teacherFromDb.setEmail(teacher.getEmail());
        teacherFromDb.setUsername(teacher.getUsername());
        teacherFromDb.setDepartment(teacher.getDepartment());
        teacherFromDb.setPhoneNumber(teacher.getPhoneNumber());
        teacherFromDb.setTeachingLevels(teacher.getTeachingLevels());
        teacherFromDb.setRole(teacher.getRole());
        teacherFromDb.setIsActive(teacher.getIsActive());
        return modelMapper.map(teacherRepository.save(teacherFromDb), TeacherRequest.class);
    }

    @Override
    public TeacherResponse teacherProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Teacher teacher = teacherRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", userDetails.getId()));

        return mapToTeacherResponse(teacher);
    }

    @Override
    public TeacherResponse getAllTeachers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase(AppConstant.SORT_DIR) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);

        List<Teacher> teachers = teacherPage.getContent();
        if (teachers.isEmpty()) {
            throw new APIException("No teachers found");
        }

        Set<TeacherResponse> teacherResponses = teachers.stream()
                .map(this::mapToTeacherResponse)
                .collect(Collectors.toSet());

        TeacherResponse response = new TeacherResponse();
        response.setContent(teacherResponses);
        response.setPageNumber(teacherPage.getNumber());
        response.setPageSize(teacherPage.getSize());
        response.setTotalElements(teacherPage.getTotalElements());
        response.setTotalPages(teacherPage.getTotalPages());
        response.setLastPage(teacherPage.isLast());
        
        return response;
    }

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setTeacherId(teacher.getId());
        response.setUsername(teacher.getUsername());
        response.setFirstName(teacher.getFirstName());
        response.setLastName(teacher.getLastName());
        response.setPhoneNumber(teacher.getPhoneNumber());
        response.setEmail(teacher.getEmail());
        
        // Map subjects to SubjectResponse DTOs
        if (teacher.getSubjects() != null && !teacher.getSubjects().isEmpty()) {
            response.setSubjects(teacher.getSubjects().stream()
                .map(subject -> modelMapper.map(subject, SubjectResponse.class))
                .collect(Collectors.toList()));
        }
        
        // Map department to DepartmentResponse DTO
        if (teacher.getDepartment() != null) {
            response.setDepartment(modelMapper.map(teacher.getDepartment(), DepartmentResponse.class));
        }
        
        response.setTeachingLevel(new HashSet<>(teacher.getTeachingLevels()));
        response.setCreatedDate(teacher.getCreatedDate());
        response.setLastModifiedDate(teacher.getLastModifiedDate());
        response.setRole(teacher.getRole().getAppRole().name());
        response.setIsActive(teacher.getIsActive());
        return response;
    }
}
