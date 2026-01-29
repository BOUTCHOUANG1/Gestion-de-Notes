package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.RevendicationResponse;
import com.university.ManageNotes.model.Revendication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {StudentMapper.class, GradeMapper.class, SemesterMapper.class})
public interface RevendicationMapper {
    
    @Mapping(target = "student", source = "student")
    @Mapping(target = "grade", source = "grade")
    @Mapping(target = "semester", source = "semester")
    RevendicationResponse toRevendicationResponse(Revendication revendication);
    
    @Mapping(target = "revendicationId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Revendication toEntity(RevendicationRequest request);
    
    RevendicationRequest toRequest(Revendication revendication);
}
