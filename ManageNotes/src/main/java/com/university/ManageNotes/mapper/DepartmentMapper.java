package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper extends BaseMapper<Department, DepartmentRequest, DepartmentResponse> {
    
    /**
     * Converts a {@link Department} entity into a {@link DepartmentResponse} dto.
     * <p>
     * Note that the {@code subjects} field is explicitly ignored in this mapping,
     * as it is not directly provided by the {@code Department} entity.
     * @param entity the department entity to convert
     * @return the converted department response
     */
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