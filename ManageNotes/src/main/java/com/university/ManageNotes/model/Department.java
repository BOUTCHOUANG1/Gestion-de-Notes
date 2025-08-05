package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an academic department.  We expose only an id & name because the current
 * user-story merely requires a list of departments so an admin can choose one after login.
 * Additional fields (code, description …) can be added later without impacting the login flow.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}
