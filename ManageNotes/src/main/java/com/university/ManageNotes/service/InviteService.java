package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.InviteCreateRequest;
import com.university.ManageNotes.dto.Response.InviteResponse;
import com.university.ManageNotes.model.InviteToken;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.repository.InviteTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class InviteService {

    @Autowired
    private InviteTokenRepository inviteTokenRepository;

    public InviteResponse createInvite(InviteCreateRequest request) {
        InviteToken token = new InviteToken();
        token.setRole(request.getRole());
        token.setExpiresAt(request.getExpiresAt() != null ? request.getExpiresAt() : Instant.now().plusSeconds(86400));
        token.setToken(UUID.randomUUID().toString().replace("-",""));
        inviteTokenRepository.save(token);
        return new InviteResponse(token.getToken());
    }

    public InviteToken consumeToken(String rawToken, Role desiredRole) {
        InviteToken token = inviteTokenRepository.findByTokenAndClaimedFalse(rawToken)
                .orElseThrow(() -> new RuntimeException("Invalid invite token"));
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Invite token expired");
        }
        if (token.getRole() != desiredRole) {
            throw new RuntimeException("Invite token not valid for role " + desiredRole);
        }
        token.setClaimed(true);
        inviteTokenRepository.save(token);
        return token;
    }
}
