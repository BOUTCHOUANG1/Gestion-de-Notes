package com.university.ManageNotes.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();

        // Extract the error messages for each fieldValue that failed validation
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });

        // Return the error messages in a map along with the BAD_REQUEST success code
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        return new ResponseEntity<>(new APIResponse(message, false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException ex) {
        String message = ex.getMessage();
        return new ResponseEntity<>(new APIResponse(message, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(
                new APIResponse("Access denied. You don't have permission to perform this action", false),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return new ResponseEntity<>(
                new APIResponse("Authentication failed. Please check your credentials", false),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Data integrity violation", ex);
        return new ResponseEntity<>(
                new APIResponse("Operation failed due to data constraint violation. Please check your data", false),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return new ResponseEntity<>(
                new APIResponse("Invalid input provided: " + ex.getMessage(), false),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIResponse> handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return new ResponseEntity<>(
                new APIResponse("Operation cannot be performed at this time", false),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred", ex);
        return new ResponseEntity<>(
                new APIResponse("An unexpected error occurred. Please try again later", false),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred", ex);
        return new ResponseEntity<>(
                new APIResponse("An unexpected error occurred. Please contact support", false),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
