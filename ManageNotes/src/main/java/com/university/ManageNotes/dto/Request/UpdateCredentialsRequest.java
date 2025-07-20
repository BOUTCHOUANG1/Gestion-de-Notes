package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCredentialsRequest {
    @NotBlank
    private String currentPassword;

    @Size(min = 3, max = 50)
    private String newUsername;

    @Size(min = 5, max = 100)
    private String newPassword;

    public String getCurrentPassword() {return currentPassword;}
    public void setCurrentPassword(String currentPassword) {this.currentPassword = currentPassword;}
    public String getNewUsername() {return newUsername;}
    public void setNewUsername(String newUsername) {this.newUsername = newUsername;}
    public String getNewPassword() {return newPassword;}
    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}
}
