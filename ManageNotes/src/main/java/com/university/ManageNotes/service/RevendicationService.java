package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;

import java.util.List;

public interface RevendicationService {
    
    RevendicationPeriodResponse create(RevendicationRequest req);
    
    List<RevendicationPeriodResponse> listClaimsForTeacher(Long teacherId);
    
    List<RevendicationPeriodResponse> listAll();
    
    List<RevendicationPeriodResponse> getClaimsForCurrentTeacher();
    
    List<RevendicationPeriodResponse> getPending();
    
    RevendicationPeriodResponse getById(Long id);
}
