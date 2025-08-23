package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.GradeClaimRequest;
import com.university.ManageNotes.dto.Response.GradeClaimResponse;
import com.university.ManageNotes.model.GradeClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {CustomMappings.class})
public interface GradeClaimMapper {
    GradeClaimMapper INSTANCE = Mappers.getMapper(GradeClaimMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "gradeId", source = "grade.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "subjectCode", source = "grade.subject.code")
    @Mapping(target = "period", source = "periodLabel")
    @Mapping(target = "currentScore", source = "grade.value")
    @Mapping(target = "status", expression = "java(claim.getStatus() != null ? claim.getStatus().name() : null)")
    @Mapping(target = "requestedScore", source = "requestedScore")
    @Mapping(target = "cause", source = "cause")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "teacherComment", source = "teacherComment")
    @Mapping(target = "resolvedAt", source = "resolvedAt")
    GradeClaimResponse toResponse(GradeClaim claim);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "periodLabel", source = "period")
    @Mapping(target = "requestedScore", source = "requestedScore")
    @Mapping(target = "cause", source = "cause")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "teacherComment", ignore = true)
    GradeClaim toEntity(GradeClaimRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "periodLabel", source = "period")
    @Mapping(target = "teacherComment", ignore = true)
    @Mapping(target = "requestedScore", source = "requestedScore")
    @Mapping(target = "cause", source = "cause")
    @Mapping(target = "description", source = "description")
    void updateFromRequest(GradeClaimRequest request, @MappingTarget GradeClaim entity);
}
