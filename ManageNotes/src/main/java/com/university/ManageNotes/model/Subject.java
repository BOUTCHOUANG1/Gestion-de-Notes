package com.university.ManageNotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Subject extends AbstractEntity{

     @Column(name = "name")
     private String name;

     @Column(name = "code")
     private String code;

     @Column(name = "credits")
     private BigDecimal credits;

     @Column(name = "coefficient")
     private BigDecimal coefficient;


     @Column(name = "idTeacher")
     private Long idTeacher;

     @OneToMany(mappedBy = "subject")
     private List<Grades> grades;

}
