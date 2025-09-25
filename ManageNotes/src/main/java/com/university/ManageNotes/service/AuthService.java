package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Response.UserResponse;
import org.springframework.security.core.Authentication;


public interface AuthService {

    UserResponse getCurrentUser(Authentication authentication);

    MessageResponse changePassword(String username, String newPassword);
}
