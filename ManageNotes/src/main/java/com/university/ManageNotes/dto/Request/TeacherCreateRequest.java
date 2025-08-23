package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Payload for creating a teacher (User story N6 – admin adds a new teacher).
 * We purposefully keep fields minimal; optional additions (address, dob …) can be appended later.
 */
@Getter
@Setter
public class TeacherCreateRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;

    @NotBlank(message = "Department is required")
    private String department;

    /** Subject IDs the teacher will teach.  Can be empty. */
    private List<Long> subjectIds = List.of();

    @Email(message = "Email must be valid")
    private String email;
}
