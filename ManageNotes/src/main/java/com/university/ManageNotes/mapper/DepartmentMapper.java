package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper extends BaseMapper<Department, DepartmentRequest, DepartmentResponse> {
    
    @Mapping(target = "subjects", ignore = true)
    DepartmentResponse toResponse(Department entity);
    
    default Department toEntity(DepartmentRequest request) {
        return Department.builder()
                .name(request.getName())
                .build();
    }
    
    default Department updateEntityFromRequest(DepartmentRequest request, Department entity) {
        entity.setName(request.getName());
        return entity;
    }
}