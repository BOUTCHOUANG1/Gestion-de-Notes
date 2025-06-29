package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassUpdateRequest {

    @Size(min = 2, max = 100, message = "Class name must be between 2 and 100 characters")
    private String name;

    @Size(min = 2, max = 50, message = "Level must be between 2 and 50 characters")
    private String level;

    private Set<Long> studentIds;
    private Set<Long> subjectIds;


}
