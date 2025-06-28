package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ClassRequest {

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 100, message = "Class name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Level is required")
    @Size(min = 2, max = 50, message = "Level must be between 2 and 50 characters")
    private String level;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;

    private Set<Long> studentIds;
    private Set<Long> subjectIds;




}
