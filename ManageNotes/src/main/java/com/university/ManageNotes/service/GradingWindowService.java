package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradingWindowRequest;
import com.university.ManageNotes.dto.Response.GradingWindowResponse;
import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.repository.GradingWindowRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradingWindowService {
    private final GradingWindowRepository windowRepository;
    private final SemesterRepository semesterRepository;

    public boolean isWindowOpen(Long semesterId, String label) {
        if (label == null) return true;
        var windows = windowRepository.findBySemesterIdAndShortNameIgnoreCase(semesterId, label);
        if (windows.isEmpty()) return false;
        LocalDate today = LocalDate.now();
        return windows.stream().anyMatch(w -> Boolean.TRUE.equals(w.getIsActive()) &&
                (w.getStartDate()==null || !today.isBefore(w.getStartDate())) &&
                (w.getEndDate()==null || !today.isAfter(w.getEndDate())));
    }
    
    public String getWindowStatusMessage(Long semesterId, String label) {
        if (label == null) return "No period specified - entry allowed";
        
        var windows = windowRepository.findBySemesterIdAndShortNameIgnoreCase(semesterId, label);
        if (windows.isEmpty()) {
            var availableWindows = windowRepository.findBySemesterIdAndIsActive(semesterId, true);
            if (availableWindows.isEmpty()) {
                return "No grading windows configured for this semester";
            }
            var openWindows = availableWindows.stream()
                .filter(w -> {
                    LocalDate today = LocalDate.now();
                    return (w.getStartDate() == null || !today.isBefore(w.getStartDate())) &&
                           (w.getEndDate() == null || !today.isAfter(w.getEndDate()));
                })
                .map(w -> w.getShortName())
                .toList();
            
            if (openWindows.isEmpty()) {
                return "Grading window '" + label + "' not found. No windows are currently open.";
            }
            return "Grading window '" + label + "' not found. Available windows: " + String.join(", ", openWindows);
        }
        
        var window = windows.getFirst();
        LocalDate today = LocalDate.now();
        
        if (!Boolean.TRUE.equals(window.getIsActive())) {
            return "Grading window '" + label + "' is disabled by administrator";
        }
        
        if (window.getStartDate() != null && today.isBefore(window.getStartDate())) {
            return "Grading window '" + label + "' opens on " + window.getStartDate();
        }
        
        if (window.getEndDate() != null && today.isAfter(window.getEndDate())) {
            return "Grading window '" + label + "' closed on " + window.getEndDate();
        }
        
        return "Grading window '" + label + "' is open until " + 
               (window.getEndDate() != null ? window.getEndDate().toString() : "further notice");
    }

    public List<GradingWindowResponse> getAllWindows() {
        return windowRepository.findAllByOrderByOrderAsc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public GradingWindowResponse createWindow(GradingWindowRequest request) {
        GradingWindow window = new GradingWindow();
        window.setName(request.getName());
        window.setShortName(request.getShortName());
        window.setType(request.getType());
        window.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        window.setStartDate(request.getStartDate());
        window.setEndDate(request.getEndDate());
        window.setColor(request.getColor());
        window.setIsActive(request.getIsActive());
        window.setOrder(request.getOrder());
        
        GradingWindow saved = windowRepository.save(window);
        return convertToResponse(saved);
    }

    public GradingWindowResponse updateWindow(Long id, GradingWindowRequest request) {
        GradingWindow window = windowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grading window not found"));
        
        window.setName(request.getName());
        window.setShortName(request.getShortName());
        window.setType(request.getType());
        window.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        window.setStartDate(request.getStartDate());
        window.setEndDate(request.getEndDate());
        window.setColor(request.getColor());
        window.setIsActive(request.getIsActive());
        window.setOrder(request.getOrder());
        
        GradingWindow saved = windowRepository.save(window);
        return convertToResponse(saved);
    }

    public void deleteWindow(Long id) {
        windowRepository.deleteById(id);
    }

    private GradingWindowResponse convertToResponse(GradingWindow window) {
        GradingWindowResponse response = new GradingWindowResponse();
        response.setId(window.getId());
        response.setName(window.getName());
        response.setShortName(window.getShortName());
        response.setType(window.getType());
        response.setSemester(window.getSemester() != null ? 
                (window.getSemester().getName().contains("1") ? 1 : 2) : null);
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
