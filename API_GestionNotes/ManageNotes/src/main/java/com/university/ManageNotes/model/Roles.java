package com.university.ManageNotes.model;

import com.university.ManageNotes.model.enums.AppRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    @Column(length = 20, name = "role_name")
    private AppRole appRole;
}
