package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.StudentRequest;
import org.springframework.transaction.annotation.Transactional;


public interface StudentService {
    @Transactional
    StudentRequest updateStudent(Long studentId, StudentRequest request);

}