package com.university.ManageNotes.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Named("CustomMappings")
public class CustomMappings {
    
    @Named("mapPeriodLabel")
    public String mapPeriodLabel(String period) {
        // If period is null or empty, return null
        if (period == null || period.trim().isEmpty()) {
            return null;
        }
        return period;
    }
    
    @Named("mapRequestedScore")
    public Double mapRequestedScore(Double score) {
        return score != null ? score : 0.0;
    }
}
