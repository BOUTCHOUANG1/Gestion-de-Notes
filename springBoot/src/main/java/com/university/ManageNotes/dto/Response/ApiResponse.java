package com.university.ManageNotes.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    @Getter
    @Setter
    private boolean success;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private T data;
    @Setter
    @Getter
    private String error;
    @Setter
    private LocalDateTime timestamp;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setError(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

}
