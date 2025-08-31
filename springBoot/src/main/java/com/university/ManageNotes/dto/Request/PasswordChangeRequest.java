package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class PasswordChangeRequest {

    @Getter
    @Setter
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    private String newPassword;
    
    @Getter
    @Setter
    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
    
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
