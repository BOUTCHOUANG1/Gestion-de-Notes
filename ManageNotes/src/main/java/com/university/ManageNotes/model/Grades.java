package com.university.ManageNotes.model;

import jakarta.persistence.Column;

import javax.security.auth.Subject;

public class Grades extends AbstractEntity{

 @Column(name = "value")
 private Double value;

 @Column(name = "coefficient")
 private Double coefficient;

 @Column(name = "comments")
 private String comments;

 @Column(name = "User")
 private Students User;

 @Column(name = "subject")
 private Subject subject;

 @Column(name = "semesters")
 private Semesters semesters;

 @Column(name = "gradeType")
 private GradeType gradeType;
}
