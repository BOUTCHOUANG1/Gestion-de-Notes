package com.university.ManageNotes.dto.Response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.ErrorResponse;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, List<String>> validationErrors;

    public ValidationErrorResponse() {
        super();
        this.setError("Validation Failed");
        this.setStatus(400);
    }

    public ValidationErrorResponse(Map<String, List<String>> validationErrors) {
        this();
        this.validationErrors = validationErrors;
        this.setMessage("One or more fields have validation errors");
    }
}
