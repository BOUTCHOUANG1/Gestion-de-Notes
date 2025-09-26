package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.StudentCycle;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private Set<Roles> role;

    // Student fields
    private TeachingLevel level;  // Single level for students
    private String matricule;
    private String speciality;
    private StudentCycle cycle;
    private LocalDate dateOfBirth;
    private String placeOfBirth;

    // Teacher fields
    private List<TeachingLevel> levels;
    private Department department;
    private String phone;
    private List<Subject> subjects;
}
