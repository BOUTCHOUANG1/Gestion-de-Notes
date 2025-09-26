package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.service.GradeService;
import com.university.ManageNotes.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class TeacherController {

    private final TeacherService teacherService;
    private final GradeService gradeService;

    @PutMapping("/admin/teacher/{id}")
    @Operation(summary = "Update Teacher information (Admin only)")
    public ResponseEntity<TeacherRequest> updateTeacher(
            @PathVariable Long id, 
            @Valid @RequestBody TeacherRequest request) {
        return new ResponseEntity<>(teacherService.updateTeacher(id, request), HttpStatus.OK);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current Teacher profile", description = "This endpoint provide the informations of the current logged in Teacher")
    public ResponseEntity<TeacherResponse> getTeacherDetails(Authentication authentication) {
        TeacherResponse teacherProfileReponse = teacherService.teacherProfile(authentication);
        return new ResponseEntity<>(teacherProfileReponse, HttpStatus.OK);
    }

    @GetMapping("/teacher/my-grades")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "Get teacher's grades", description = "Get all grades entered by current teacher")
    public ResponseEntity<List<GradeResponse>> getTeacherGrades() {
        return new ResponseEntity<>(gradeService.getTeacherGrades(), HttpStatus.OK);
    }
}