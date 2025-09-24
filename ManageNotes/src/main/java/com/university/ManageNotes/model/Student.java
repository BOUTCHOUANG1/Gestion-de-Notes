package com.university.ManageNotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "place_of_birth"),
                @UniqueConstraint(columnNames = "speciality")
        })
public class Student extends Users {
    @OneToOne
    @JoinColumn(name = "level_id")
    @NotBlank(message = "Level is required")
    private TeachingLevel studentLevel;

    private String matricule;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Grades> grades = new ArrayList<>();

    @Column(name = "speciality")
    @NotBlank(message = "Speciality is required")
    @Size(min = 5, message = "Speciality must be at least 5 characters long")
    private String speciality;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Cycle is required")
    private StudentCycle cycle;

    @Column(name = "date_of_birth")
    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    @NotBlank(message = "Place of birth is required")
    @Size(min = 5, message = "Place of birth must be at least 5 characters long")
    private String placeOfBirth;

    @OneToOne(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private Transcript transcript;

    @JsonIgnore
    @OneToMany(mappedBy = "revendicatedBy", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    orphanRemoval = true)
    private List<Revendication> revendications = new ArrayList<>();
}
