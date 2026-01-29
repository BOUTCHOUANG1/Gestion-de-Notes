package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.enums.StudentLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachingLevelResponse {
    private Long teachingLevelId;
    private StudentLevel studentLevel;
}