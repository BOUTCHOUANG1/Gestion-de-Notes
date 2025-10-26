package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revendication_period", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"exam_period_id", "semester_id"}))
public class RevendicationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revendicationPeriodId;

    @ManyToOne
    @JoinColumn(name = "exam_period_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String color;

    private Boolean isActive = false;

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}
