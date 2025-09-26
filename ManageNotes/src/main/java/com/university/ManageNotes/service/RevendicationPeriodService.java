package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.model.Exam;

import java.util.List;

public interface RevendicationPeriodService {
    
    Boolean isPeriodOpen(Long semesterId, Exam exam);
    
    String getPeriodStatusMessage(Long semesterId, Exam exam);
    
    List<RevendicationPeriodResponse> getAllPeriods();

    RevendicationPeriodResponse createWindow(RevendicationPeriodRequest request);

    RevendicationPeriodResponse updateWindow(Long id, RevendicationPeriodRequest request);
    
    void deleteWindow(Long id);
    
    List<RevendicationPeriodResponse> getActiveWindows();
    
    List<RevendicationPeriodResponse> getAllPeriod();
    
    RevendicationPeriodResponse createPeriod(RevendicationPeriodRequest request);
    
    RevendicationPeriodRequest updatePeriod(Long id, RevendicationPeriodRequest request);
    
    void deletePeriod(Long id);
    
    List<RevendicationPeriodResponse> getActivePeriods();
}
