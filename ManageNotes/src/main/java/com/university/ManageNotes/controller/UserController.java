package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.UpdateCredentialsRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Lookup", description = "Utility endpoints for user/role lookup")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.university.ManageNotes.repository.StudentRepository studentRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public com.university.ManageNotes.dto.Response.UserProfileResponse me(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        return com.university.ManageNotes.dto.Response.UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @PostMapping("/me/credentials")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public MessageResponse updateCredentials(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody UpdateCredentialsRequest request) {
        if (request.getNewPassword() != null && !request.getNewPassword().equals(request.getConfirmPassword())) {
            return MessageResponse.error("Passwords do not match");
        }
        return authService.updateCredentials(principal.getId(), request.getCurrentPassword(), request.getNewUsername(), request.getNewPassword());
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "List students visible to current user")
    public java.util.List<UserProfileResponse> students(@AuthenticationPrincipal UserPrincipal principal) {
        java.util.List<com.university.ManageNotes.model.Students> students;
        if (principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            students = studentRepository.findAll();
        } else {
            students = studentRepository.findStudentsByTeacherSubject(principal.getId());
        }
        return students.stream().map(s -> UserProfileResponse.builder()
                .id(s.getId())
                .username(s.getMatricule())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .email(s.getEmail())
                .role(Role.STUDENT)
                .build()).toList();
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all teachers")
    public java.util.List<UserProfileResponse> teachers() {
        return userRepository.findByRole(Role.TEACHER).stream()
                .map(u -> UserProfileResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .build())
                .toList();
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user by ID (Admin only)")
    public com.university.ManageNotes.dto.Response.MessageResponse deleteUser(@PathVariable Long id) {
        var userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return com.university.ManageNotes.dto.Response.MessageResponse.error("User not found");
        }
        var user = userOpt.get();

        // If the user is a student, also remove the corresponding student record
        if (user.getRole() == Role.STUDENT) {
            studentRepository.findByEmail(user.getEmail()).ifPresent(studentRepository::delete);
        }

        userRepository.deleteById(id);
        return com.university.ManageNotes.dto.Response.MessageResponse.success("User deleted successfully");
    }
}
