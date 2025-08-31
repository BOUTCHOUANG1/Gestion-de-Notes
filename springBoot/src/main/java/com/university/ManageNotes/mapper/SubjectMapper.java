package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @org.mapstruct.Builder(disableBuilder = true))
public interface SubjectMapper extends BaseMapper<Subject, SubjectRequest, SubjectResponse> {

    @Mapping(target = "semesterId", source = "semester.id")
    @Mapping(target = "semesterName", source = "semester.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "teacherId", source = "idTeacher")
    SubjectResponse toResponse(Subject entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "idTeacher", source = "teacherId")
    @Mapping(target = "credits", expression = "java(java.math.BigDecimal.valueOf(request.getCredits()))")
    Subject toEntity(SubjectRequest request);
    
    default Subject updateEntityFromRequest(SubjectRequest request, Subject entity) {
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setCredits(BigDecimal.valueOf(request.getCredits()));
        entity.setIdTeacher(request.getTeacherId());
        entity.setActive(request.getActive());
        entity.setLevel(request.getLevel());
        entity.setCycle(request.getCycle());
        return entity;
    }
}
