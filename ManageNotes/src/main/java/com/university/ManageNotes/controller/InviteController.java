package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.InviteCreateRequest;
import com.university.ManageNotes.dto.Response.InviteResponse;
import com.university.ManageNotes.service.InviteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InviteResponse> createInvite(@Valid @RequestBody InviteCreateRequest request) {
        return ResponseEntity.ok(inviteService.createInvite(request));
    }
}
