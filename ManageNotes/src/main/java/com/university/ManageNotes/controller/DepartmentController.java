package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.BaseCrudService;
import com.university.ManageNotes.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "CRUD operations and department-specific functionality")
public class DepartmentController extends BaseCrudController<Long, DepartmentRequest, DepartmentResponse> {
    
    private final DepartmentService departmentService;

    @Override
    protected BaseCrudService<?, Long, DepartmentRequest, DepartmentResponse> service() {
        return departmentService;
    }

    @PostMapping("/switch/{deptId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Switch user's department context")
    public MessageResponse switchDept(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long deptId) {
        return departmentService.switchDepartment(principal.getId(), deptId);
    }

    @Override
    public MessageResponse create(@RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartmentWithSubjects(request);
        return new MessageResponse("Department created successfully", "SUCCESS", response);
    }

    @GetMapping("/{deptId}/details")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get department details with subjects")
    public DepartmentResponse getDepartmentDetails(@PathVariable Long deptId) {
        return departmentService.getDepartmentDetails(deptId);
    }
    
    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public java.util.List<DepartmentResponse> all() {
        return departmentService.getAllDepartmentsSortedByDate();
    }
}
