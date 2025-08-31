package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeClaimDecisionRequest {
    @NotNull
    private Boolean approve;
    private String comment;
}
