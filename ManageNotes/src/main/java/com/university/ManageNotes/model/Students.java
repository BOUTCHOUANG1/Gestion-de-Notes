package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "students")
public class Students extends AbstractEntity {

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "studentNumber", unique = true)
    private String studentNumber;

    @Column(name = "level")
    private String level;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Grades> grades;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "cycle")
    private String cycle;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    // Lombok will generate getters and setters
}
