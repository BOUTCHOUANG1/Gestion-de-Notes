package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.userProfileResDto;
import com.university.ManageNotes.service.AuthService;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Unified profile endpoint for all authenticated users")
public class ProfileController {
    private final AuthService authService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Returns profile information of the currently authenticated user based on JWT token")
    public ResponseEntity<userProfileResDto> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(authService.getProfileByUsername(userDetails.getUsername()), HttpStatus.OK);
    }
}
