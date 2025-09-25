package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.model.ExamPeriod;

import java.util.List;

public interface RevendicationPeriodService {
    
    Boolean isPeriodOpen(Long semesterId, ExamPeriod examPeriod);
    
    String getPeriodStatusMessage(Long semesterId, ExamPeriod examPeriod);
    
    List<RevendicationPeriodResponse> getAllPeriods();
    
    GradingWindowResponse createWindow(RevendicationPeriodRequest request);
    
    GradingWindowResponse updateWindow(Long id, RevendicationPeriodRequest request);
    
    void deleteWindow(Long id);
    
    List<GradingWindowResponse> getActiveWindows();
}
