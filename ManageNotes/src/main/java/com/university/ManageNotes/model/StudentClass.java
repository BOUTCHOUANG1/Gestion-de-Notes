package com.university.ManageNotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentClass extends  AbstractEntity{

    @Column(name = "nameStudent")
    private String nameStudent;

    @Column(name = "student")
    private List<Users>  students;

    @Column(name = "subject")
     private List<Subject> subjects;
}
