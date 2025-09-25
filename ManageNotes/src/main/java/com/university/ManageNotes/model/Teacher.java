package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
public class Teacher extends Users {
    @Column(length = 9)
    @NotBlank(message = "Teacher's phone number is required")
    @Size(min = 9, max = 9, message = "Teacher's phone number must be 9 digits long")
    private String phoneNumber;

    @NotBlank(message = "Teacher's subjects is required")
    @Size(min = 5, message = "Teacher's subjects must be at least 5 characters long")
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "subject_id")
    private List<Subject> subjects;

    @OneToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teachingLevel_id")
    private List<TeachingLevel> teachingLevel = new ArrayList<>();


    @OneToMany(mappedBy = "examiner")
    private List<Grades> gradesEntered = new ArrayList<>();
}
