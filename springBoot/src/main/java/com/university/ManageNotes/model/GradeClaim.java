package com.university.ManageNotes.model;

import com.university.ManageNotes.util.PeriodLabelUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "grade_claims")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GradeClaim extends BaseRequest {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grades grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    private Semesters semester;

    @Column(name = "period_label")
    private String periodLabel;

    public void setPeriodLabel(String periodLabel){
        this.periodLabel = PeriodLabelUtil.normalize(periodLabel);
    }

    @Column(name = "requested_score", nullable = false)
    private Double requestedScore;

    @Column(columnDefinition = "TEXT")
    private String cause;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "teacher_comment", columnDefinition = "TEXT")
    private String teacherComment;
}
