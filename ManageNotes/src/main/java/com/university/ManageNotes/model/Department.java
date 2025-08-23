package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an academic department. Currently, exposes only id & name to support
 * department listing for admin selection after login. Additional fields can be
 * added later without impacting the login flow.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "departments")
@ToString(onlyExplicitlyIncluded = true)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @ToString.Include
    private String name;

    // Inherited from AbstractEntity:
    // - id
    // - createdDate
    // - lastModifiedDate
}
