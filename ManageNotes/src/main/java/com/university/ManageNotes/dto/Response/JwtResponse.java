package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private List<String> authorities;

    // Additional constructor used by AuthService
    public JwtResponse(String token, Long id, String email, String firstName, String lastName, List<String> authorities) {
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
    }
}
