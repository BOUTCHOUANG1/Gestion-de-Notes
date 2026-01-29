package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;

import java.util.List;

public interface DepartmentService{

    List<DepartmentResponse> getAllDepartments(Integer pageNumber,
                                               Integer pageSize,
                                               String sortBy, String sortOrder);
    DepartmentResponse createDepartment(DepartmentRequest request);

    DepartmentResponse updateDepartment(DepartmentRequest departmentRequest, Long departmentId);

    DepartmentResponse deleteDepartment(Long departmentId);
}

