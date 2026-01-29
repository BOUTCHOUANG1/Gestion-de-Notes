package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.*;
import com.university.ManageNotes.model.enums.AssessmentType;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GradeResponse {
    private Long gradeId;
    private Double ccScore;
    private Double snScore;
    private Double totalScore;
    private Double maxValue;
    private String comments;
    private SimpleStudentResponse student;
    private SimpleSubjectResponse subject;
    private SimpleTeacherResponse examiner;
    private SimpleSemesterResponse semester;
    private AssessmentType exam;
    private List<SimpleRevendicationResponse> revendication;
    private Boolean hasPassed;
    private Double gpa;
    private Instant createdDate;
    private Instant lastModifiedDate;
    
    // Simple DTOs to avoid circular references
    @Data
    public static class SimpleStudentResponse {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String matricule;
    }
    
    @Data
    public static class SimpleSubjectResponse {
        private Long id;
        private String subjectName;
        private String subjectCode;
        private Double credits;
    }
    
    @Data
    public static class SimpleTeacherResponse {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
    }
    
    @Data
    public static class SimpleSemesterResponse {
        private Long id;
        private String name;
        private Boolean active;
    }
    
    @Data
    public static class SimpleRevendicationResponse {
        private Long id;
        private Double requestedScore;
        private String cause;
        private String status;
        private String description;
    }
}
