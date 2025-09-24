package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.model.AssessmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
public class GradeResponse {
    private GradeRequest content;
}
