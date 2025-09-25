package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.model.Students;

import java.util.List;

public interface GradeService {

    GradeRequest updateGrade(Long gradeId, GradeUpdateRequest gradeRequest);
    
    MessageResponse deleteGrade(Long id);
    
    TranscriptResponse calculateSemesterSummary(Long studentId, Long semesterId);
    
    TranscriptResponse calculateYearSummary(Long studentId, Long semester1Id, Long semester2Id);
    
    GradeResponse createGrade(GradeRequest gradeRequest);
    
    StudentGradesResponse getStudentGrades(Long userId, Long semesterId);
    
    List<GradeResponse> getTeacherGrades();
    
    GradeSheetResponse getGradeSheet(String subjectCode, Long semesterId, String period);
    
    List<Students> getStudentsBySemester(Long semesterId);
}