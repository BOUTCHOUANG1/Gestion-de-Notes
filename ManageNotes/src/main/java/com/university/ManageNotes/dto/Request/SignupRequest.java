package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.*;
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
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 15, message = "Email must not exceed 15 characters")
    private String email;

    @Size(min = 5, max = 9, message = "Password must be between 5 and 9 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    private Set<String> role;

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
