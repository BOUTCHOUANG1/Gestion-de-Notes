package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private com.university.ManageNotes.model.StudentLevel level;
    private List<String> levels;
    private List<SubjectResponse> subjects;
}
