package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "grades")
public class Grades{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    @NotBlank(message = "Score is required")
    @Min(value = 0, message = "Score must be greater than or equal to 0")
    private Double score;

    @NotBlank(message = "Max value is required")
    @Min(value = 0, message = "Max value must be greater than or equal to 0")
    @Max(value = 100, message = "Max value must be less than or equal to 100")
    private Double maxValue;

    @NotBlank(message = "Comments are required")
    @Size(min = 5, max = 255, message = "Comments must be between 5 and 255 characters long")
    private String comments;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher examiner;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type")
    private AssessmentType exam;

    @OneToMany(mappedBy = "grades", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    orphanRemoval = true)
    private List<Revendication> revendication = new ArrayList<>();

    @CreatedDate
    @Column(name ="creation_date",nullable = false,updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    private Boolean hasPassed;

    private Double gpa;

    public Grades(Double score, Double maxValue, Student student, Subject subject, String comments, Teacher examiner, Semester semester, AssessmentType exam) {
        this.score = score;
        this.maxValue = maxValue;
        this.student = student;
        this.subject = subject;
        this.comments = comments;
        this.examiner = examiner;
        this.semester = semester;
        this.exam = exam;
    }
}
