package com.university.ManageNotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectPerformance extends AbstractEntity{

    private String subjectName;
    private Double average;
    private Integer totalStudents;


}
