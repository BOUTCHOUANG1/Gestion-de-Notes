package com.university.ManageNotes.model;

import com.university.ManageNotes.dto.Request.GradeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkError {
    private Long index;
    private String error;
    private GradeRequest failedGrade;


}
