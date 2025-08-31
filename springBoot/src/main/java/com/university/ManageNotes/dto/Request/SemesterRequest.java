package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRequest {
    
    private Long id;
    
    @NotBlank(message = "Semester name is required")
    @Size(min = 1, max = 50, message = "Semester name must be between 1 and 50 characters")
    private String name;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Boolean active = true;
}