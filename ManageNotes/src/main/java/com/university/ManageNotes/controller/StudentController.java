package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin-specific operations")
public class StudentController {

    private final StudentService studentService;

    @PutMapping("/student/{id}")
    @Operation(summary = "Update Student information (Admin only)")
    public ResponseEntity<StudentRequest> updateStudent(
            @PathVariable Long id, 
            @Valid @RequestBody StudentRequest request) {
        return new ResponseEntity<>(studentService.updateStudent(id, request), HttpStatus.OK);
    }
}