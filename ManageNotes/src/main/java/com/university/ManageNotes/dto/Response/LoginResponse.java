package com.university.ManageNotes.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class LoginResponse {
    private Long id;

    private String username;

    private String role;

    private String token;

    private Instant createdDate;

    private Instant lastModifiedDate;


    public LoginResponse(Long id, String username, String role, String token) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.token = token;
    }
}


