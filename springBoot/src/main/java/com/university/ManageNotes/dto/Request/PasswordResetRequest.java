package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

}
