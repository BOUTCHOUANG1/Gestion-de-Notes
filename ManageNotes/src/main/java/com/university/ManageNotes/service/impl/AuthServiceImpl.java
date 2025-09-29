package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.LoginResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AppRole;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.security.JwtUtils;
import com.university.ManageNotes.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public MessageResponse changePassword(String username, String newPassword) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException("Error: User is not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);

        return new MessageResponse("Password changed successfully!");
    }

    @Override
    public MessageResponse logout() {
        ResponseCookie cookie = jwtUtils.getClearJwtCookie();
        ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));

        return new MessageResponse("You've been signed out!");
    }

    @Override
    public MessageResponse register(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new APIException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new APIException("Error: Email is already in use!");
        }

        Users user = new Users();
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setMustChangePassword(true);
        user.setIsActive(true);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedDate(Instant.now());
        // Handle role assignment based on role string
        // This will need to be implemented based on your role lookup logic

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new LoginResponse(null, null, "Bad credentials", null);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtToken(authentication);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        // Fetch user to get audit dates
        Users user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new APIException("User not found"));

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), role, jwtToken);
        response.setCreatedDate(user.getCreatedDate());
        response.setLastModifiedDate(user.getLastModifiedDate());
        
        return response;
    }

    @Override
    public UserResponse getCurrentAdmin(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserResponse userResponse = new UserResponse();

        if (roles.contains("ADMIN")) {
            Users admin = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin", "adminId", userDetails.getId()));

            return getAdminResponse(admin);
        } else {
            throw new APIException("User role not recognized");
        }
    }

    private static UserResponse getAdminResponse(Users admin) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(admin.getUsername());
        userResponse.setFirstName(admin.getFirstName());
        userResponse.setLastName(admin.getLastName());
        userResponse.setEmail(admin.getEmail());
        userResponse.setRole(admin.getRole());
        userResponse.setIsActive(admin.getIsActive());
        return userResponse;
    }

}
