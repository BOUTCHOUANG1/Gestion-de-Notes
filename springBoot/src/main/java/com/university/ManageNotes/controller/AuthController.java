package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.PasswordChangeRequest;
import com.university.ManageNotes.dto.Request.PasswordResetRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.JwtResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            // include mustChangePassword in response
            UserPrincipal userPrincipal = authService.getUserPrincipalByUsername(loginRequest.getUsername());
            jwtResponse.setMustChangePassword(userPrincipal != null && userPrincipal.isMustChangePassword());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Authentication failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            MessageResponse response = authService.registerUser(signupRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            if (!passwordChangeRequest.isPasswordMatching()) {
                return ResponseEntity.badRequest()
                        .body(MessageResponse.error("Password confirmation does not match"));
            }
            
            MessageResponse response = authService.changePassword(
                    userPrincipal.getUsername(),
                    passwordChangeRequest.getNewPassword()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Password change failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            return ResponseEntity.ok(MessageResponse.success("Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Logout failed: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            MessageResponse resp = authService.resetPassword(request.getEmail());
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Password reset failed: " + ex.getMessage()));
        }
    }
}
