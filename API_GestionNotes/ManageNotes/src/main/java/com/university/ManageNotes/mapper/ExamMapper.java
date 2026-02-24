package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Response.ExamResponse;
import com.university.ManageNotes.model.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExamMapper {
    
    ExamResponse toExamResponse(Exam exam);
    
    Exam toEntity(ExamResponse response);
}
