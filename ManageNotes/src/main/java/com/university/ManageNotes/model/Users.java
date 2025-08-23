package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "Users")
public class Users extends AbstractEntity {
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "must_change_password")
    private Boolean mustChangePassword = true;

    // --- Extra fields for teacher profile (US N6) ---
    @Column(name = "phone", length = 30)
    private String phone;

    // Simple department name reference; we avoid full FK to keep migration light.
    @Column(name = "department", length = 100)
    private String department;

    // Levels that a teacher can teach (stored as JSON array)
    @ElementCollection
    @CollectionTable(name = "user_levels", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "level")
    private List<String> levels;

    @OneToMany(mappedBy = "enteredBy")
    private List<Grades> gradesEntered;
}
