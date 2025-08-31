package com.university.ManageNotes.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    @Getter
    @Setter
    private String message;
    private String errorCode;
    @Getter
    @Setter
    private int status;
    @Setter
    private LocalDateTime timestamp;
    @Setter
    private List<FieldError> fieldErrors;


    public ErrorResponse(String message, String errorCode, int status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }


    public static class FieldError {
        private String field;
        @Setter
        @Getter
        private String message;
        private Object rejectedValue;


        public FieldError(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }

    }
}
