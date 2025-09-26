package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.TeachingLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TeacherRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<Subject> subjects;
    private Department department;
    private List<TeachingLevel> teachingLevel;
    private Roles appRole;
    private Boolean isActive;
}