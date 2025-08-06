package com.university.ManageNotes.controller;

// ... existing code ... <imports>
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Navigation", description = "Endpoints for switching department context")
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List departments")
    public List<Department> list() {
        return service.list();
    }

    @PostMapping("/switch/{deptId}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse switchDept(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long deptId) {
        return service.switchDepartment(principal.getId(), deptId);
    }
}
