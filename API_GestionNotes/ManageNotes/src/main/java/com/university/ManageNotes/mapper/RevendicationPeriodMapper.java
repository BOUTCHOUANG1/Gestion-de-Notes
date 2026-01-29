package com.university.ManageNotes.mapper;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.model.RevendicationPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ExamMapper.class, SemesterMapper.class})
public interface RevendicationPeriodMapper {
    
    @Mapping(target = "exam", source = "exam")
    @Mapping(target = "semester", source = "semester")
    RevendicationPeriodResponse toRevendicationPeriodResponse(RevendicationPeriod revendicationPeriod);
    
    @Mapping(target = "revendicationPeriodId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "semester", ignore = true)
    RevendicationPeriod toEntity(RevendicationPeriodRequest request);
    
    RevendicationPeriodRequest toRequest(RevendicationPeriod revendicationPeriod);
}
