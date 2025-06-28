package com.university.ManageNotes.dto.Response;


import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassResponse extends AbstractEntity {
    private String name;
    private String level;
    private Long semesterId;
    private String semesterName;
    private List<UserResponse> students;
    private List<SubjectResponse> subjects;

    private Integer totalStudents;


}
