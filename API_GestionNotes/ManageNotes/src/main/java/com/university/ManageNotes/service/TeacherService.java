package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface TeacherService {

    TeacherRequest updateTeacher(Long teacherId, TeacherRequest request);

    TeacherResponse teacherProfile(Authentication authentication);

    List<TeacherResponse> getAllTeachers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    
    Map<String, List<StudentResponse>> getStudentsByTeachingLevels(Authentication authentication);

}