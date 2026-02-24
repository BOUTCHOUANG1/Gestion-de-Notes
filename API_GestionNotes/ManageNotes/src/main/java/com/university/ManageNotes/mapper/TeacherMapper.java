package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.TeacherRequest;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {DepartmentMapper.class})
public interface TeacherMapper {
    
    @Mapping(target = "teacherId", source = "id")
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "department", source = "department")
    @Mapping(target = "role", expression = "java(teacher.getRole() != null ? teacher.getRole().getAppRole().name() : null)")
    TeacherResponse toTeacherResponse(Teacher teacher);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "gradesEntered", ignore = true)
    Teacher toEntity(TeacherRequest request);
    
    TeacherRequest toRequest(Teacher teacher);
}
