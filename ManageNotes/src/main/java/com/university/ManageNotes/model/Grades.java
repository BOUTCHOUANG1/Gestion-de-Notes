package com.university.ManageNotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.security.auth.Subject;

@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grades extends AbstractEntity{

 @Column(name = "value")
 private Double value;

 @Column(name = "coefficient")
 private Double coefficient;

 @Column(name = "comments")
 private String comments;

 @ManyToOne
 @JoinColumn(name = "idStudents")
 private  Students students;

 @Column(name = "subject")
 private Subject subject;

 @ManyToOne
 @JoinColumn(name = "idSemester")
 private   Semesters semesters;

 @Column(name = "gradeType")
 private GradeType gradeType;
}
