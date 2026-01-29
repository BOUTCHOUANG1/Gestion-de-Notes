package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;
import com.university.ManageNotes.model.Semester;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {SubjectMapper.class, GradeMapper.class})
public interface SemesterMapper {
    
    @Mapping(target = "subjects", source = "subjects")
    @Mapping(target = "grades", source = "grades")
    SemesterResponse toSemesterResponse(Semester semester);
    
    @Mapping(target = "semesterId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "revendications", ignore = true)
    Semester toEntity(SemesterRequest request);
    
    SemesterRequest toRequest(Semester semester);
}
