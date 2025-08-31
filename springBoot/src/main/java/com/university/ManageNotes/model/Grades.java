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

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private PeriodType periodType; // e.g., CC_1, CC_2, SN_1, SN_2

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


    public Semesters getSemesters() {
        return this.semester;
    }

    public void setSemesters(Semesters semesters) {
        this.semester = semesters;
    }

    public void setPeriodType(PeriodType periodType){
        this.periodType = periodType;
    }

}
