package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "students")
public class Students extends AbstractEntity {

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "matricule", unique = true)
    private String matricule;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private StudentLevel level;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Grades> grades;

    @Column(name = "speciality")
    private String speciality;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle")
    private StudentCycle cycle;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    // Lombok will generate getters and setters
}
