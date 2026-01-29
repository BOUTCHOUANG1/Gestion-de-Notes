package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.model.*;
import org.mapstruct.*;

/**
 * MapStruct mapper for Grade entity and DTOs.
 * Handles bidirectional mapping between Grades entity, GradeRequest, and GradeResponse.
 * Includes nested mappings for related entities to avoid circular references.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeMapper {
    
    // ==================== Entity to Response DTO ====================
    
    /**
     * Maps Grades entity to GradeResponse DTO.
     * Custom mappings for nested entities using qualified methods.
     */
    @Mapping(source = "student", target = "student", qualifiedByName = "toSimpleStudentResponse")
    @Mapping(source = "subject", target = "subject", qualifiedByName = "toSimpleSubjectResponse")
    @Mapping(source = "examiner", target = "examiner", qualifiedByName = "toSimpleTeacherResponse")
    @Mapping(source = "semester", target = "semester", qualifiedByName = "toSimpleSemesterResponse")
    @Mapping(source = "exam.assessmentType", target = "exam")
    @Mapping(source = "revendication", target = "revendication", qualifiedByName = "toSimpleRevendicationResponseList")
    GradeResponse toGradeResponse(Grades grade);
    
    // ==================== Request DTO to Entity ====================
    
    /**
     * Maps GradeRequest DTO to Grades entity.
     * Note: Relationships must be set manually in service layer using IDs.
     */
    @Mapping(target = "gradeId", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "examiner", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "revendication", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "hasPassed", ignore = true)
    @Mapping(target = "gpa", ignore = true)
    Grades toEntity(GradeRequest request);
    
    // ==================== Entity to Request DTO (for service returns) ====================
    
    /**
     * Maps Grades entity back to GradeRequest DTO.
     * Used when services need to return Request DTOs (temporary - will be fixed).
     */
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.subjectId", target = "subjectId")
    @Mapping(source = "exam.examPeriodId", target = "examId")
    @Mapping(source = "semester.semesterId", target = "semesterId")
    @Mapping(source = "exam.assessmentType", target = "assessmentType")
    GradeRequest toRequest(Grades grade);
    
    // ==================== Nested Mapping Methods ====================
    
    /**
     * Maps Student entity to SimpleStudentResponse.
     */
    @Named("toSimpleStudentResponse")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "matricule", target = "matricule")
    GradeResponse.SimpleStudentResponse toSimpleStudentResponse(Student student);
    
    /**
     * Maps Subject entity to SimpleSubjectResponse.
     */
    @Named("toSimpleSubjectResponse")
    @Mapping(source = "subjectId", target = "id")
    @Mapping(source = "subjectName", target = "subjectName")
    @Mapping(source = "subjectCode", target = "subjectCode")
    @Mapping(source = "credits", target = "credits")
    GradeResponse.SimpleSubjectResponse toSimpleSubjectResponse(Subject subject);
    
    /**
     * Maps Teacher entity to SimpleTeacherResponse.
     */
    @Named("toSimpleTeacherResponse")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    GradeResponse.SimpleTeacherResponse toSimpleTeacherResponse(Teacher teacher);
    
    /**
     * Maps Semester entity to SimpleSemesterResponse.
     */
    @Named("toSimpleSemesterResponse")
    @Mapping(source = "semesterId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "active", target = "active")
    GradeResponse.SimpleSemesterResponse toSimpleSemesterResponse(Semester semester);
    
    /**
     * Maps Revendication entity to SimpleRevendicationResponse.
     */
    @Named("toSimpleRevendicationResponse")
    @Mapping(source = "revendicationId", target = "id")
    @Mapping(source = "requestedScore", target = "requestedScore")
    @Mapping(source = "description", target = "cause")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    GradeResponse.SimpleRevendicationResponse toSimpleRevendicationResponse(Revendication revendication);
    
    /**
     * Maps list of Revendication entities to list of SimpleRevendicationResponse.
     */
    @Named("toSimpleRevendicationResponseList")
    @IterableMapping(qualifiedByName = "toSimpleRevendicationResponse")
    java.util.List<GradeResponse.SimpleRevendicationResponse> toSimpleRevendicationResponseList(
        java.util.List<Revendication> revendications);
}
