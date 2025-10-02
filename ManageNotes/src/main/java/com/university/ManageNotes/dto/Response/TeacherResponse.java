package com.university.ManageNotes.dto.Response;


import com.university.ManageNotes.model.TeachingLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"subjects", "department"})
public class TeacherResponse {
    private Long teacherId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @JsonIgnore
    private List<SubjectResponse> subjects;
    private DepartmentResponse department;
    private Set<TeachingLevel> teachingLevel;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private String role;
    private Boolean isActive;
}
