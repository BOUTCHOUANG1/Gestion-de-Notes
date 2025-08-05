package com.university.ManageNotes.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GradeReport extends AbstractEntity {


    private Long idStudents;

    private Long  idSemesters;

    private Double gpa;

    private String status;

    private String  pdfPath;

}
