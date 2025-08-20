package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.GradeClaim;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GradeClaimResponse {
    private Long id;
    private Long gradeId;
    private Long studentId;
    private String subjectCode;
    private String period;
    private Double currentScore;
    private Double requestedScore;
    private String cause;
    private String description;
    private String status;
    private String teacherComment;
    private LocalDateTime resolvedAt;

    public static GradeClaimResponse fromEntity(GradeClaim c) {
        GradeClaimResponse r = new GradeClaimResponse();
        r.setId(c.getId());
        r.setGradeId(c.getGrade().getId());
        r.setStudentId(c.getStudent().getId());
        r.setSubjectCode(c.getGrade().getSubject().getCode());
        r.setPeriod(c.getPeriodLabel());
        r.setCurrentScore(c.getGrade().getValue());
        r.setRequestedScore(c.getRequestedScore());
        r.setCause(c.getCause());
        r.setDescription(c.getDescription());
        r.setStatus(c.getStatus().name());
        r.setTeacherComment(c.getTeacherComment());
        r.setResolvedAt(c.getResolvedAt());
        return r;
    }
}
