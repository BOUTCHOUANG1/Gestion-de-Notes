package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.StudentRequest;
import com.university.ManageNotes.dto.Response.StudentResponse;
import com.university.ManageNotes.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {GradeMapper.class})
public interface StudentMapper {
    
    @Mapping(target = "grades", source = "grades")
    @Mapping(target = "role", expression = "java(student.getRole() != null ? student.getRole().getAppRole().name() : null)")
    StudentResponse toStudentResponse(Student student);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "grades", ignore = true)
    @Mapping(target = "transcript", ignore = true)
    @Mapping(target = "revendications", ignore = true)
    Student toEntity(StudentRequest request);
    
    StudentRequest toRequest(Student student);
}
