package com.university.ManageNotes.model;

import com.university.ManageNotes.util.ExaminationPeriodsUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_claims")
public class Revendication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revendicationId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_revendication",
            joinColumns = @JoinColumn(name = "revendication_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> student = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "period_id")
    private ExamPeriod period;

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grades grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "requested_score", nullable = false)
    @Min(value = 0, message = "Requested score must be greater than or equal to 0")
    @NotBlank(message = "Requested score is required")
    private Double requestedScore;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    private String description;

    @Column(name = "teacher_comment", columnDefinition = "TEXT")
    @NotBlank(message = "Teacher comment is required")
    private String teacherComment;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private String rejectionReason;

    private LocalDateTime requestedAt;

    private LocalDateTime resolvedAt;

    @ManyToOne
    @JoinColumn(name = "revendicated_by")
    private Student revendicatedBy;
}
