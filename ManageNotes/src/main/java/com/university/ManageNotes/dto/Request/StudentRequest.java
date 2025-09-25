package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.TeachingLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
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