package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.TeacherRepository;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import com.university.ManageNotes.service.TeacherService;
import com.university.ManageNotes.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    private final ResponseMapper responseMapper;

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
    @Transactional
    public TeacherResponse teacherProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Teacher teacher = teacherRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", userDetails.getId()));
        
        // Force initialization of lazy collections within transaction
        if (teacher.getTeachingLevels() != null) {
            teacher.getTeachingLevels().size();
        }
        if (teacher.getDepartment() != null && teacher.getDepartment().getSubjects() != null) {
            teacher.getDepartment().getSubjects().size();
        }
        if (teacher.getSubjects() != null) {
            teacher.getSubjects().size();
        }

        return mapToTeacherResponse(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAllTeachers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        List<Teacher> allTeachers = teacherRepository.findAllWithDepartmentSubjects();
        
        if (allTeachers.isEmpty()) {
            throw new APIException("No teachers found");
        }

        // Manual pagination and sorting
        Comparator<Teacher> comparator = getComparator(sortBy, sortOrder);
        List<Teacher> sortedTeachers = allTeachers.stream()
                .sorted(comparator)
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return sortedTeachers.stream()
                .map(this::mapToTeacherResponse)
                .collect(Collectors.toList());
    }
    
    private Comparator<Teacher> getComparator(String sortBy, String sortOrder) {
        Comparator<Teacher> comparator = switch (sortBy.toLowerCase()) {
            case "firstname" -> Comparator.comparing(Teacher::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase));
            case "lastname" -> Comparator.comparing(Teacher::getLastName, Comparator.nullsLast(String::compareToIgnoreCase));
            case "email" -> Comparator.comparing(Teacher::getEmail, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> Comparator.comparing(Teacher::getId);
        };
        return sortOrder.equalsIgnoreCase("desc") ? comparator.reversed() : comparator;
    }

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = responseMapper.toTeacherResponseBasic(teacher);
        
        if (teacher.getDepartment() != null) {
            Department dept = teacher.getDepartment();
            // Manually load subjects
            List<com.university.ManageNotes.model.Subject> subjects = subjectRepository.findByDepartment(dept);
            dept.setSubjects(new java.util.HashSet<>(subjects));
            response.setDepartment(responseMapper.toDepartmentResponse(dept));
        }
        
        return response;
    }

    @Override
    public Map<String, List<StudentResponse>> getStudentsByTeachingLevels(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Teacher teacher = teacherRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", userDetails.getId()));
        
        Map<String, List<StudentResponse>> studentsByLevel = new HashMap<>();
        
        for (TeachingLevel teachingLevel : teacher.getTeachingLevels()) {
            List<Student> students = studentRepository.findByStudentLevel(teachingLevel);
            
            List<StudentResponse> studentResponses = students.stream()
                    .map(this::mapToStudentResponse)
                    .collect(Collectors.toList());
            
            studentsByLevel.put(teachingLevel.getStudentLevel().name(), studentResponses);
        }
        
        return studentsByLevel;
    }
    
    private StudentResponse mapToStudentResponse(Student student) {
        return modelMapper.map(student, StudentResponse.class);
    }
}
