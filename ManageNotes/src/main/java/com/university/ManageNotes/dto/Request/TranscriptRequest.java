package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Semester;
import com.university.ManageNotes.model.Student;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class TranscriptRequest {
    private Student student;
    private Semester semester;
    private String format; // "PDF"
    private Boolean includeComments = true;

    // new metadata filled by teacher/admin
    private String facultyName;
    private String academicYear;

}
