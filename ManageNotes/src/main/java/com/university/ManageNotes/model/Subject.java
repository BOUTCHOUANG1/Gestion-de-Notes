package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Subject extends AbstractEntity {
     @Column(name = "name")
     private String name;
     @Column(name = "code")
     private String code;
     @Column(name = "credits")
     private BigDecimal credits;
     @Column(name = "idTeacher")
     private Long idTeacher;
     @Column(name = "description", length = 500)
     private String description;
     @Column(name = "active")
     private Boolean active = true;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades;
}
