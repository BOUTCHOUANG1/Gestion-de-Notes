package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.GradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeSheetColumn {
    private GradeType type;
    private String label;
}
