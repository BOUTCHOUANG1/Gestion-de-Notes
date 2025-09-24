package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    private Long departmentId;
    private String departmentName;
    private Set<Subject> subjects;
}