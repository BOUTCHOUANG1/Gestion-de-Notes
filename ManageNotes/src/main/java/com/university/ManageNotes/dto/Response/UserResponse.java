package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Roles appRole;

    private Boolean isActive;

    // Teacher fields
    private List<TeachingLevel> levels;
    private Department department;
    private String phone;
    private List<Subject> subjects;

    // Student fields
    private TeachingLevel level;  // Single level for students
    private String matricule;
    private String speciality;
    private StudentCycle cycle;
    private LocalDate dateOfBirth;
    private String placeOfBirth;

}
