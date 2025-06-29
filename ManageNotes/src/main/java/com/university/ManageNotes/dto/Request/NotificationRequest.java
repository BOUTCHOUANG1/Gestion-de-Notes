package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotEmpty(message = "At least one recipient is required")
    private List<Long> recipientIds;

    private String type = "INFO"; // INFO, WARNING, SUCCESS, ERROR
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT
    private Boolean emailNotification = false;


}
