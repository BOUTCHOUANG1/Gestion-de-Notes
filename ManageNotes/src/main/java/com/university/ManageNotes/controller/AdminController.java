package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.TeacherCreateRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Teacher-Subject Management", description = "Mapping between teachers and subjects")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/Sub-teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creating a new teacher and signing it to subjects")
    public MessageResponse createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        return userService.createTeacher(request);
    }
}
