package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.PasswordChangeRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.LoginResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.RoleRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.JwtUtils;
import com.university.ManageNotes.service.AuthService;
import com.university.ManageNotes.service.impl.AuthServiceImpl;
import com.university.ManageNotes.service.impl.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Authenticate a user information", description = "This endpoint authenticate a particular user from the login credentials")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a user information", description = "This endpoint is an Admin priviledge to Register a particular user")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: username is already taken"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken"));
        }

        Users user = new Users(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles = signupRequest.getRole();

        Set<Roles> roles = new HashSet<>();

        Teacher teacher = modelMapper.map(signupRequest, Teacher.class);

        Student student = modelMapper.map(signupRequest, Student.class);

        if(strRoles == null || strRoles.isEmpty()) {
            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new APIException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new APIException("Error: Role is not found"));
                        roles.add(adminRole);
                    }
                    case "teacher" -> {
                        Roles teacherRole = roleRepository.findByRoleName(AppRole.ROLE_TEACHER)
                                .orElseThrow(() -> new APIException("Error: Role is not found"));
                        roles.add(teacherRole);
                        teacher.setTeachingLevel(signupRequest.getLevels());
                        teacher.setEmail(signupRequest.getEmail());
                        teacher.setPhoneNumber(signupRequest.getPhone());
                        userRepository.save(teacher);
                    }
                    default -> {
                        Roles studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                                .orElseThrow(() -> new APIException("Error: Role is not found"));
                        roles.add(studentRole);
                        student.setFirstName(signupRequest.getFirstName());
                        student.setLastName(signupRequest.getLastName());
                        student.setEmail(signupRequest.getEmail());
                        student.setMatricule(signupRequest.getMatricule() != null ? signupRequest.getMatricule() : UUID.randomUUID().toString());
                        student.setStudentLevel((signupRequest.getLevel()));
                        student.setCycle(signupRequest.getCycle());
                        student.setSpeciality(signupRequest.getSpeciality());
                        student.setPlaceOfBirth(signupRequest.getPlaceOfBirth());
                        student.setDateOfBirth(signupRequest.getDateOfBirth());
                        studentRepository.save(student);
                    }
                }
            });

            if (roles.equals(AppRole.ROLE_STUDENT)) {
                Student st = studentRepository.findByEmail(signupRequest.getEmail())
                        .orElseThrow(() -> new APIException("Error: Student not found"));
                if (st != null && !signupRequest.getUsername().equals(st.getMatricule())) {
                    signupRequest.setUsername(st.getMatricule());
                    userRepository.save(st);
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "This endpoint provide the informations of the current logged in user")
    public ResponseEntity<UserResponse> getUserDetails(Authentication authentication) {
        UserResponse userProfileReponse = authService.getCurrentUser(authentication);
        return new ResponseEntity<>(userProfileReponse, HttpStatus.OK);
    }

    @PostMapping("/password")
    @Operation(summary = "Update a user's password information", description = "This endpoint update a particular user's password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                                            @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            if (!passwordChangeRequest.isPasswordMatching()) {
                return ResponseEntity.badRequest()
                        .body( new MessageResponse("Password confirmation does not match"));
            }
            
            MessageResponse response = authService.changePassword(
                    userPrincipal.getUsername(),
                    passwordChangeRequest.getNewPassword()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Password change failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out a user", description = "This endpoint log out a particular user from the system")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtUtils.getClearJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
