package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.BulkError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BulkGradeResponse {
    private Integer totalProcessed;
    private Integer successCount;
    private Integer errorCount;
    private List<GradeResponse> successfulGrades;
    private List<BulkError> errors;

}
