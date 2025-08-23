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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    // Lombok will generate getters and setters
}
