package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subjects",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "subject_name"),
        @UniqueConstraint(columnNames = "subject_code")
})
public class Subject{
    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long subjectId;

     @NotBlank(message = "Subject name is required")
     @Size(min = 5, message = "Subject name must be at least 5 characters long")
     private String subjectName;

     @NotBlank(message = "Subject code is required")
     @Size(min = 3, max = 20, message = "Subject code must be between 3 and 20 characters")
     @Pattern(regexp = "^[A-Z0-9]+$", message = "Subject code must contain only uppercase letters and numbers")
     private String subjectCode;

     @NotBlank(message = "Credits are required")
     @Min(value = 1, message = "Credits must be at least 1")
     @Max(value = 10, message = "Credits must not exceed 10")
     private BigDecimal credits;

     @OneToOne
     @JoinColumn(name = "teacher_id")
     private Teacher teacher;

     @Column(length = 500)
     @NotBlank(message = "Description is required")
     @Size(min = 5, message = "Description must be at least 5 characters long")
     private String description;

     @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
     @JoinColumn(name = "subjectLevel_id")
     private List<TeachingLevel> subjectsLevel = new ArrayList<>();

     @Enumerated(EnumType.STRING)
     @NotNull(message = "Cycle is required")
     private StudentCycle Studentcycle;

     @ManyToOne
     @JoinColumn(name = "semester_id")
     private Semester semester;

     @ManyToOne
     @JoinColumn(name = "departmentId")
     private Department department;

     @ManyToOne
     @JoinColumn(name = "transcript_id")
     private Transcript transcript;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades;

     @OneToMany(mappedBy = "subject")
     private List<Revendication> revendication = new ArrayList<>();
}
