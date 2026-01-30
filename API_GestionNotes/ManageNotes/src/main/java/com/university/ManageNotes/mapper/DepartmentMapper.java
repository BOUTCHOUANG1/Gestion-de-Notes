package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {
    
    @Mapping(target = "departmentSubjects", ignore = true)
    DepartmentResponse toDepartmentResponse(Department department);
    
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Department toEntity(DepartmentRequest request);
    
    DepartmentRequest toRequest(Department department);
}
