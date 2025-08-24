package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import com.university.ManageNotes.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends AbstractEntity {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    // phone removed

    private Role role;

    private Boolean active;

    // Teacher fields
    private List<String> levels;
    private String department;
    private String phone;

    // Student fields
    private String level;  // Single level for students
    private String matricule;
    private String speciality;
    private String cycle;
    private java.time.LocalDate dateOfBirth;
    private String placeOfBirth;

    public void setId(Long id) {
        super.setId(id);
    }
}
