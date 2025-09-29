package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import com.university.ManageNotes.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Override
    public StudentRequest updateStudent(Long studentId, StudentRequest request) {
        // Map DTO to Entity
        Student student = modelMapper.map(request, Student.class);

        Student studentFromDb = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Update fields
        if (student.getFirstName() != null) {
            studentFromDb.setFirstName(student.getFirstName());
        }
        if (student.getLastName() != null) {
            studentFromDb.setLastName(student.getLastName());
        }
        if (student.getEmail() != null) {
            studentFromDb.setEmail(student.getEmail());
        }
        if (student.getMatricule() != null) {
            studentFromDb.setMatricule(student.getMatricule());
            studentFromDb.setUsername(student.getMatricule());
        }
        if (student.getStudentLevel() != null) {
            studentFromDb.setStudentLevel(student.getStudentLevel());
        }
        if (student.getSpeciality() != null) {
            studentFromDb.setSpeciality(student.getSpeciality());
        }
        if (student.getCycle() != null) {
            studentFromDb.setCycle(student.getCycle());
        }
        if (student.getDateOfBirth() != null) {
            studentFromDb.setDateOfBirth(student.getDateOfBirth());
        }
        if (student.getPlaceOfBirth() != null) {
            studentFromDb.setPlaceOfBirth(student.getPlaceOfBirth());
        }

        // Save entity and map to request DTO
        Student savedStudent = studentRepository.save(studentFromDb);
        return modelMapper.map(savedStudent, StudentRequest.class);
    }

    @Override
    public StudentResponse studentProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Student student = studentRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", userDetails.getId()));

        // Map entity to response DTO
        StudentResponse response = modelMapper.map(student, StudentResponse.class);
        
        // Map grades to response DTOs
        if (student.getGrades() != null && !student.getGrades().isEmpty()) {
            response.setGrades(student.getGrades().stream()
                .map(grade -> modelMapper.map(grade, GradeResponse.class))
                .collect(Collectors.toList()));
        }

        return response;
    }

    @Override
    public StudentResponse getAllStudents(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase(AppConstant.SORT_DIR) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Student> studentPage = studentRepository.findAll(pageable);

        List<Student> students = studentPage.getContent();
        if (students.isEmpty()) {
            throw new APIException("No students found");
        }

        List<StudentRequest> studentRequests = students.stream()
                .map(student -> modelMapper.map(student, StudentRequest.class))
                .collect(Collectors.toList());

        StudentResponse response = new StudentResponse();
        response.setContent(studentRequests);
        response.setPageNumber(studentPage.getNumber());
        response.setPageSize(studentPage.getSize());
        response.setTotalElements(studentPage.getTotalElements());
        response.setTotalPages(studentPage.getTotalPages());
        response.setLastPage(studentPage.isLast());

        return response;
    }
}