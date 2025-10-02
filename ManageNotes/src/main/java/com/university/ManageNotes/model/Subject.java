package com.university.ManageNotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.university.ManageNotes.model.enums.StudentCycle;
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

     private String subjectName;

     private String subjectCode;

     private BigDecimal credits;

     @JsonIgnore
     @ManyToOne
     @JoinColumn(name = "teacher_id")
     private Teacher teacher;

     @Column(length = 500)
     private String description;

     @ManyToOne
     @JoinColumn(name = "teaching_level_id")
     private TeachingLevel subjectLevel;

     @Enumerated(EnumType.STRING)
     private StudentCycle Studentcycle;

     @ManyToOne
     @JoinColumn(name = "semester_id")
     private Semester semester;

     @ManyToOne
     @JoinColumn(name = "department_id")
     private Department department;

     @ManyToOne
     @JoinColumn(name = "transcript_id")
     private Transcript transcript;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades = new ArrayList<>();


}
