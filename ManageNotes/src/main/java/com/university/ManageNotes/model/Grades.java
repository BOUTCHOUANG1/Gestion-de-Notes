package com.university.ManageNotes.model;

import com.university.ManageNotes.util.PeriodLabelUtil;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grades extends AbstractEntity{

    @Column(name = "value")
    private Double value;

    @Column(name="max_value")
    private Double maxValue = 20.0;

    @Column(name = "Comments")
    private String comments;

    @Column(name = "period_label")
    private String periodLabel; // e.g., "CC #2", "SN #1"

    @ManyToOne
    @JoinColumn(name = "id_students")
    private Students student;

    @ManyToOne
    @JoinColumn(name = "id_subject")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users enteredBy;

    @ManyToOne
    @JoinColumn(name = "id_semester")
    private Semesters semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type")
    private GradeType type;

    // Backward compatibility methods
    public GradeType getGradeType() {
        return this.type;
    }

    public void setGradeType(GradeType gradeType) {
        this.type = gradeType;
    }

    public Semesters getSemesters() {
        return this.semester;
    }

    public void setSemesters(Semesters semesters) {
        this.semester = semesters;
    }

    public void setPeriodLabel(String periodLabel){
        this.periodLabel = PeriodLabelUtil.normalize(periodLabel);
    }

    public Students getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public Double getValue() {
        return value;
    }

    public String getComments() {
        return comments;
    }

    public Users getEnteredBy() {
        return enteredBy;
    }

    public GradeType getType() {
        return type;
    }
}
