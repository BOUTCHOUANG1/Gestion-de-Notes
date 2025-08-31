package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.StudentUpdateRequest;
import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public UserProfileResponse updateStudent(Long userId, StudentUpdateRequest request) {
        // Find user first
        var user = userRepository.findById(userId)
                .filter(u -> u.getRole() == Role.STUDENT)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + userId));
        
        // Find corresponding student record
        var student = studentRepository.findByMatricule(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Student record not found for user: " + user.getUsername()));

        // Update Users entity
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getMatricule());
        userRepository.save(user);

        // Update Students entity
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setMatricule(request.getMatricule());
        student.setLevel(StudentLevel.valueOf(request.getLevel().toUpperCase()));
        student.setSpeciality(request.getSpeciality());
        if (request.getCycle() != null) {
            student.setCycle(StudentCycle.valueOf(request.getCycle().toUpperCase()));
        }
        studentRepository.save(student);

        // Update user username to match new matricule if changed
        if (!user.getUsername().equals(request.getMatricule())) {
            user.setUsername(request.getMatricule());
            userRepository.save(user);
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(Role.STUDENT)
                .build();
    }

    @Transactional
    public SubjectResponse updateSubject(Long subjectId, SubjectRequest request) {
        var subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));
        
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setCredits(request.getCredits() != null ? BigDecimal.valueOf(request.getCredits()) : null);
        subject.setDescription(request.getDescription());
        subject.setActive(request.getActive());
        
        // Set level and cycle
        if (request.getLevel() != null) {
            subject.setLevel(request.getLevel());
        }
        
        if (request.getCycle() != null) {
            subject.setCycle(request.getCycle());
        }
        
        // Handle teacher assignment
        if (request.getTeacherId() != null) {
            subject.setIdTeacher(request.getTeacherId());
        }
        
        var saved = subjectRepository.save(subject);
        
        return SubjectResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .code(saved.getCode())
                .credits(saved.getCredits())
                .description(saved.getDescription())
                .active(saved.getActive())
                .level(saved.getLevel())
                .cycle(saved.getCycle())
                .build();
    }
}