package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponse extends AbstractEntity {
    private String name;
    private Boolean active;
    private Long totalStudents;
    private Long totalGrades;
}
