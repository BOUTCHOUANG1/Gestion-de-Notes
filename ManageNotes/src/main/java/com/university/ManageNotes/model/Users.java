package com.university.ManageNotes.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true )
@Table(name = "Users")

public class Users extends AbstractEntity {
    //private String username;
    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "student")
    private List<Grades> gradesReceived;

    @OneToMany(mappedBy = "enteredBy")
    private List<Grades>gradesEntered;

}
