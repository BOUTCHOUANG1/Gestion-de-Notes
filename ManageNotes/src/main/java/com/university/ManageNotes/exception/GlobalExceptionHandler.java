package com.university.ManageNotes.exception;

import com.university.ManageNotes.dto.Response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  org.springframework.http.HttpStatusCode status,
                                                                  WebRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapToFieldError)
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse("Validation failed", "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value());
        error.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse.FieldError mapToFieldError(FieldError fieldError) {
        return new ErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
