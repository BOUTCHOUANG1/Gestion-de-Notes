package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_teacher_level", columnNames = {"id_teacher", "level"})
})
public class Subject extends AbstractEntity {
     @Column(name = "name")
     private String name;

     @Column(name = "code")
     private String code;

     @Column(name = "credits")
     private BigDecimal credits;

     @Column(name = "id_teacher")
     private Long idTeacher;

     @Column(name = "description", length = 500)
     private String description;

     @Enumerated(EnumType.STRING)
     @Column(name = "level")
     private StudentLevel level;

     @Enumerated(EnumType.STRING)
     @Column(name = "cycle")
     private StudentCycle cycle;

     @ManyToOne
     @JoinColumn(name = "id_semester")
     private Semesters semester;

     @ManyToOne
     @JoinColumn(name = "department_id")
     private Department department;

     @Column(name = "active")
     private Boolean active = true;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades;
}
