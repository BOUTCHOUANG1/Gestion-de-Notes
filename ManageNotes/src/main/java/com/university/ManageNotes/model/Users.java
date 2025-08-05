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

    @OneToMany(mappedBy = "enteredBy")
    private List<Grades> gradesEntered;
}
