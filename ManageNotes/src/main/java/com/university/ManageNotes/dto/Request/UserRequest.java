package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    // phone removed

    @NotNull(message = "Role is required")
    private Role role;

    private Boolean active = true;

    private String password;

    // For teachers: levels they can teach
    private List<String> levels;

    // For teachers: department
    private String department;

    // For teachers: phone
    private String phone;

    // For students: their academic level
    private String level;

    // For students: additional fields
    private String matricule;
    private String speciality;
    private String cycle;

}
