package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HealthCheckResponse {
    private String status; // "UP", "DOWN", "DEGRADED"
    private LocalDateTime timestamp;
    private String version;
    private Long uptime;
    private Map<String, ComponentHealth> components;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComponentHealth {
        private String status;
        private String message;
        private Map<String, Object> details;

        public ComponentHealth(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
