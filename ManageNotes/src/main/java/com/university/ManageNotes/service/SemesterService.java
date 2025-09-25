package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SemesterRequest;
import com.university.ManageNotes.dto.Response.SemesterResponse;

import java.util.List;

public interface SemesterService {

    List<SemesterResponse> getAllSemesters();
    
    SemesterResponse getSemesterById(Long id);
    
    SemesterResponse createSemester(SemesterRequest request);
    
    SemesterResponse updateSemester(Long id, SemesterRequest request);
    
    void updateSemesters(List<SemesterRequest> requests);
    
    void deleteSemester(Long id);
}