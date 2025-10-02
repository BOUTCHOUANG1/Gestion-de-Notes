package com.university.ManageNotes.controller;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.service.StudentService;
import com.university.ManageNotes.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class AdminController {

    private final TeacherService teacherService;
    private final StudentService studentService;

    @PutMapping("/admin/teacher/{id}")
    @Operation(summary = "Update Teacher information (Admin only)")
    public ResponseEntity<TeacherRequest> updateTeacher(
            @PathVariable Long id, 
            @Valid @RequestBody TeacherRequest request) {
        return new ResponseEntity<>(teacherService.updateTeacher(id, request), HttpStatus.OK);
    }

    @GetMapping("/admin/teachers")
    @Operation(summary = "Get all teachers (Admin only)", description = "Retrieve all teachers with pagination")
    public ResponseEntity<List<TeacherResponse>> getAllTeachers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_TEACHER_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(teacherService.getAllTeachers(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/admin/students")
    @Operation(summary = "Get all students (Admin only)", description = "Retrieve all students with pagination")
    public ResponseEntity<List<StudentResponse>> getAllStudents(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_STUDENT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(studentService.getAllStudents(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }
}