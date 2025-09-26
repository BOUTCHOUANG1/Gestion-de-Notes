package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.TeachingLevel;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private String username;
    private String password;
    private Roles appRole;
    private String firstName;
    private String lastName;
    private String email;
    private TeachingLevel studentLevel;
    private StudentCycle cycle;
    private String matricule;
    private String speciality;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
}