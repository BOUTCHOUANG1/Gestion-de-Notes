package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubjectMapper extends com.university.ManageNotes.mapper.BaseMapper<Subject, SubjectRequest, SubjectResponse> {

    @org.mapstruct.Mapping(target = "semesterId", source = "semester.id")
    @org.mapstruct.Mapping(target = "semesterName", source = "semester.name")
    SubjectResponse toResponse(Subject entity);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "semester", ignore = true)
    Subject toEntity(SubjectRequest request);
}
