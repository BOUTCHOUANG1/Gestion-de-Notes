package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DepartmentMapper.class, SemesterMapper.class})
public interface SubjectMapper {
    
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "semester", source = "semester")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "studentCycle", source = "studentcycle")
    SubjectResponse toSubjectResponse(Subject subject);
    
    @Mapping(target = "subjectId", ignore = true)
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "transcript", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "subjectLevel", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "department", ignore = true)
    Subject toEntity(SubjectRequest request);
    
    SubjectRequest toRequest(Subject subject);
}
