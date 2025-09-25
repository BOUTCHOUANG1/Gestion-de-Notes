package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.AppRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive;
}
