package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.model.GradeType;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    public MessageResponse addGrade(GradeRequest gradeRequest) {
        try {
            // Add grade logic here
            return MessageResponse.success("Grade added successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to add grade: " + e.getMessage());
        }
    }

    public GradeResponse updateGrade(Long gradeId, GradeUpdateRequest gradeRequest) {
        try {
            // Update grade logic here
            return new GradeResponse();
        } catch (Exception e) {
            return null;
        }
    }

    public MessageResponse deleteGrade(Long gradeId) {
        try {
            // Delete grade logic here
            return MessageResponse.success("Grade deleted successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to delete grade: " + e.getMessage());
        }
    }

    public List<GradeResponse> getGradesByStudent(Long studentId) {
        // Implementation here
        return List.of();
    }

    public List<GradeResponse> getGradesBySubject(Long subjectId) {
        // Implementation here
        return List.of();
    }

    private GradeResponse convertToResponse(Grades grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setSubjectId(grade.getSubject().getId());
        response.setValue(grade.getValue());
        response.setCoefficient(grade.getCoefficient());
        response.setType(grade.getType()); // This will use the setGradeType method we added
        response.setComments(grade.getComments());
        response.setEnteredBy(grade.getEnteredBy().getId()); // Fix: Convert User to Long ID
        response.setEnteredByName(grade.getEnteredBy().getFirstName() + " " + grade.getEnteredBy().getLastName());

        return response;
    }

    public GradeResponse createGrade(GradeRequest gradeRequest) {
        // Implementation to create grade
        return new GradeResponse(); // Placeholder
    }

    public StudentGradesResponse getStudentGrades(Long studentId, Long semesterId) {
        // Implementation to get student grades
        return new StudentGradesResponse(); // Placeholder
    }

    public List<GradeResponse> getTeacherGrades() {
        // Implementation to get teacher grades
        return List.of(); // Placeholder
    }
}
