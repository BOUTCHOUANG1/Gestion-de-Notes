package com.university.ManageNotes.model;

import jakarta.persistence.Column;

import java.util.List;

public class StudentClass extends  AbstractEntity{

    @Column(name = "nameStudent")
    private String nameStudent;

    @Column(name = "student")
    private List<Users>  students;

    @Column(name = "subject")
     private List<Subject> subjects;
}
