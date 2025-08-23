package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper extends BaseMapper<Department, DepartmentRequest, DepartmentResponse> {
    
    @Mapping(target = "subjects", ignore = true)
    DepartmentResponse toResponse(Department entity);
    
    @Mapping(target = "id", ignore = true)
    Department toEntity(DepartmentRequest request);
    
    void updateEntityFromRequest(DepartmentRequest request, @MappingTarget Department entity);
}