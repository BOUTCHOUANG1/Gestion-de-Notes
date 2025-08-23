package com.university.ManageNotes.model;

import com.university.ManageNotes.dto.Request.GradeRequest;
import lombok.Value;
import com.fasterxml.jackson.annotation.JsonInclude;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkError {
    Long index;
    String error;
    GradeRequest failedGrade;

    @Override
    public String toString() {
        return String.format("BulkError(index=%d, error='%s', failedGrade=%s)",
                             index, error, failedGrade);
    }
}
