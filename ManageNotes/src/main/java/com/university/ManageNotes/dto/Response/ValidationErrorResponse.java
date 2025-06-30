package com.university.ManageNotes.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, List<String>> validationErrors;

    public ValidationErrorResponse(Map<String, List<String>> validationErrors) {
        super("Validation Failed", "One or more fields have validation errors", 400);
        this.validationErrors = validationErrors;
        this.setTimestamp(LocalDateTime.now());
    }
}
