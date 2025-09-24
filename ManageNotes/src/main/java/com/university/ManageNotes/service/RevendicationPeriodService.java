package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.model.ExamPeriod;
import com.university.ManageNotes.model.RevendicationPeriod;

import java.util.List;
import java.util.stream.Collectors;


public interface RevendicationPeriodService {
    Boolean isPeriodOpen(Long semesterId, ExamPeriod examPeriod);
    String getPeriodStatusMessage(Long semesterId, ExamPeriod examPeriod);
    List<RevendicationPeriodResponse> getAllPeriods();

    public GradingWindowResponse createWindow(RevendicationPeriodRequest request) {
        RevendicationPeriod window = new RevendicationPeriod();
        window.setName(request.getName());
        window.setShortName(request.getShortName());
        window.setPeriodLabel(request.getPeriodLabel());
        window.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        window.setStartDate(request.getStartDate());
        window.setEndDate(request.getEndDate());
        window.setColor(request.getColor());
        window.setIsActive(request.getIsActive());
        window.setOrder(request.getOrder());
        
        RevendicationPeriod saved = periodRepository.save(window);
        return convertToResponse(saved);
    }

    public GradingWindowResponse updateWindow(Long id, RevendicationPeriodRequest request) {
        RevendicationPeriod window = periodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grading window not found"));
        
        window.setName(request.getName());
        window.setShortName(request.getShortName());
        window.setPeriodLabel(request.getPeriodLabel());
        window.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        window.setStartDate(request.getStartDate());
        window.setEndDate(request.getEndDate());
        window.setColor(request.getColor());
        window.setIsActive(request.getIsActive());
        window.setOrder(request.getOrder());
        
        RevendicationPeriod saved = periodRepository.save(window);
        return convertToResponse(saved);
    }

    public void deleteWindow(Long id) {
        periodRepository.deleteById(id);
    }

    public List<GradingWindowResponse> getActiveWindows() {
        return periodRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GradingWindowResponse convertToResponse(RevendicationPeriod window) {
        GradingWindowResponse response = new GradingWindowResponse();
        response.setId(window.getId());
        response.setName(window.getName());
        response.setShortName(window.getShortName());
        response.setPeriodLabel(window.getPeriodLabel());
        response.setSemester(window.getSemester() != null ? 
                window.getSemester().getId().intValue() : null);
        response.setStartDate(window.getStartDate());
        response.setEndDate(window.getEndDate());
        response.setColor(window.getColor());
        response.setIsActive(window.getIsActive());
        response.setOrder(window.getOrder());
        response.setCreatedDate(window.getCreatedDate());
        response.setLastModifiedDate(window.getLastModifiedDate());
        return response;
    }
}
