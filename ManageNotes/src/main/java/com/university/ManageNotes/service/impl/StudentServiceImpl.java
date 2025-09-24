package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
