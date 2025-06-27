package com.university.ManageNotes.model;

import javax.security.auth.Subject;

public class Grades extends AbstractEntity{

 private Double value;

 private Double coefficient;

 private String comments;

 private Students User;

 private Subject subject;

 private Semesters semesters;

 private GradeType gradeType;
}
