package com.university.ManageNotes.dto.Response;

import lombok.Data;
import java.util.List;

@Data
public class DepartmentResponse {
    private Long id;
    private String name;
    private List<SubjectResponse> subjects;
}
