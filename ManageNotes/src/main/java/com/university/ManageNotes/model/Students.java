package com.university.ManageNotes.model;

import jakarta.persistence.Column;

public class Students extends  AbstractEntity{

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "level")
    private  String level;

    @Column(name = "idSemester")
    private Long idSemester;

    @Column(name = "idSubject")
    private  Long idSubject;
}
