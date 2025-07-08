package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.Role;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InviteCreateRequest {
    @NotNull
    private Role role;

    @Future
    private Instant expiresAt;
}
