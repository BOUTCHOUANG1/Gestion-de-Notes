package com.university.ManageNotes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private String phoneNumber;

    @ToString.Exclude
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subject> subjects;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private List<TeachingLevel> teachingLevels = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "examiner")
    private List<Grades> gradesEntered = new ArrayList<>();
}
