package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.TranscriptRequest;
import com.university.ManageNotes.dto.Response.TranscriptResponse;
import com.university.ManageNotes.model.Transcript;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {StudentMapper.class, SemesterMapper.class, SubjectMapper.class})
public interface TranscriptMapper {
    
    TranscriptResponse toTranscriptResponse(Transcript transcript);
    
    @Mapping(target = "transcriptId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "gpa", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Transcript toEntity(TranscriptRequest request);
    
    TranscriptRequest toRequest(Transcript transcript);
}
