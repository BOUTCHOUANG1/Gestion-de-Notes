package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class GradeClaim extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "idStudent")
    private Students student;

    @ManyToOne
    @JoinColumn(name = "idGrade")
    private Grades grade;

    @ManyToOne
    @JoinColumn(name = "idSemester")
    private Semesters semester;

    @Column(name = "period_label")
    private String periodLabel; // CC, SN, etc.

    @Column(name = "requested_score")
    private Double requestedScore;

    @Column(name = "cause")
    private String cause;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClaimStatus status = ClaimStatus.PENDING;

    @Column(name = "teacher_comment", length = 1000)
    private String teacherComment;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public enum ClaimStatus {PENDING, APPROVED, REJECTED}
}
