package com.university.ManageNotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Semesters extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "active")
    private Boolean active;

    @JsonIgnore
    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    private List<Grades> grades;

    public String getName() {
        return name;
    }
}
