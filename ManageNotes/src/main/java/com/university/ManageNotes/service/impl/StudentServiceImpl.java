package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.enums.StudentLevel;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.service.StudentService;
import com.university.ManageNotes.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.valueOf;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    @Override
    public StudentRequest updateStudent(Long studentId, StudentRequest request) {
        Student student = modelMapper.map(request, Student.class);

        Student studentFromDb = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        // Update Users entity
        studentFromDb.setFirstName(student.getFirstName());
        studentFromDb.setLastName(student.getLastName());
        studentFromDb.setEmail(student.getEmail());
        studentFromDb.setUsername(student.getMatricule());
        studentFromDb.setMatricule(student.getMatricule());
        studentFromDb.setStudentLevel(student.getStudentLevel() != null ? StudentLevel.valueOf(student.getStudentLevel().toString().toUpperCase()) : null);
        studentFromDb.setSpeciality(student.getSpeciality());
        studentFromDb.setCycle(student.getCycle() != null ? StudentCycle.valueOf(request.getCycle().toUpperCase()) : null);

        // Update student username to match new matricule if changed
        if (!studentFromDb.getUsername().equals(student.getMatricule())) {
            studentFromDb.setUsername(student.getMatricule());
            studentRepository.save(studentFromDb);
        }

        return modelMapper.map(studentRepository.save(studentFromDb), StudentRequest.class);
    }

    @Override
    public StudentResponse studentProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Student student = studentRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", userDetails.getId()));

        StudentRequest request = modelMapper.map(student, StudentRequest.class);

        StudentResponse response = new StudentResponse();


        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setMatricule(request.getMatricule());
        response.setStudentLevel(request.getStudentLevel());
        response.setCycle(request.getCycle());
        response.setSpeciality(request.getSpeciality());
        response.setDateOfBirth(request.getDateOfBirth());
        response.setIsActive(student.getIsActive());
        response.setSubjects(subjectRepository.findByStudentLevelAndActiveSemester(request.getStudentLevel()));
        return response;
    }

}
