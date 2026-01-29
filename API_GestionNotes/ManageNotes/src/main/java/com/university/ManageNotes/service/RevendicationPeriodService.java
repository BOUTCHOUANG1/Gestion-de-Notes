package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.model.Exam;

import java.util.List;

public interface RevendicationPeriodService {
    
    List<RevendicationPeriodResponse> getAllPeriod();
    
    RevendicationPeriodResponse createPeriod(RevendicationPeriodRequest request);
    
    RevendicationPeriodResponse updatePeriod(Long id, RevendicationPeriodRequest request);
    
    void deletePeriod(Long id);
    
    List<RevendicationPeriodResponse> getActivePeriods();
    
    Boolean isPeriodOpen(Long semesterId, Exam exam);
    
    String getPeriodStatusMessage(Long semesterId, Exam exam);
}
