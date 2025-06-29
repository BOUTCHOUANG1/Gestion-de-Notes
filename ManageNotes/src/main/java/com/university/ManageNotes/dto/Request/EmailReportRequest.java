package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailReportRequest {
    @NotNull(message = "Report ID is required")
    private Long reportId;

    @NotEmpty(message = "At least one recipient email is required")
    private List<@Email(message = "Invalid email format") String> recipientEmails;

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;

    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    // Constructors, Getters and Setters
    // ... [complete implementation]

}
