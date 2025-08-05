package com.university.ManageNotes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final com.university.ManageNotes.service.UserService userService;

    public AdminController(com.university.ManageNotes.service.UserService userService) {
        this.userService = userService;
    }

    @org.springframework.web.bind.annotation.PostMapping("/teachers")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public com.university.ManageNotes.dto.Response.MessageResponse createTeacher(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody com.university.ManageNotes.dto.Request.TeacherCreateRequest request) {
        return userService.createTeacher(request);
    }
}
