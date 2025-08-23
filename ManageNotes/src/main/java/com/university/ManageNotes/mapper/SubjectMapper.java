package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface SubjectMapper extends BaseMapper<Subject, SubjectRequest, SubjectResponse> {

    @org.mapstruct.Mapping(target = "semesterId", source = "semester.id")
    @org.mapstruct.Mapping(target = "semesterName", source = "semester.name")
    @org.mapstruct.Mapping(target = "departmentId", source = "department.id")
    @org.mapstruct.Mapping(target = "departmentName", source = "department.name")
    SubjectResponse toResponse(Subject entity);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "semester", ignore = true)
    Subject toEntity(SubjectRequest request);
    
    default Subject updateEntityFromRequest(SubjectRequest request, Subject entity) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setCredits(java.math.BigDecimal.valueOf(request.getCredits()));
        entity.setActive(request.getActive());
        entity.setLevel(request.getLevel());
        entity.setCycle(request.getCycle());
        return entity;
    }
}
