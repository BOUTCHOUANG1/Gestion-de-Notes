package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Subject Lookup", description = "Endpoints for subject lookup")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    @Operation(summary = "Get all subjects")
    public List<SubjectResponse> all() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by id")
    public SubjectResponse one(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }
}

