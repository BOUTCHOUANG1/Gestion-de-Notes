package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentService {
    @Transactional
    StudentRequest updateStudent(Long studentId, StudentRequest request);

    StudentResponse studentProfile(Authentication authentication);

    List<StudentResponse> getAllStudents(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
