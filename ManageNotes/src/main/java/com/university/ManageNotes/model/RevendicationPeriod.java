package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revendication_period")
public class RevendicationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revendicationPeriodId;

    @OneToOne
    @JoinColumn(name = "exam_period_id")
    private ExamPeriod examPeriod;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "color")
    private String color;

    @Column(name = "is_active")
    private Boolean isActive = false;

}
