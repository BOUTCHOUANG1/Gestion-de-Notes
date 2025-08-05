package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.UserRequest;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.mapper.UserMapper;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final com.university.ManageNotes.repository.SubjectRepository subjectRepository;

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest) {
        try {
            // Create user logic here
            // Hash password
            // Save to database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setUsername(userRequest.getUsername());
            response.setEmail(userRequest.getEmail());
            response.setFirstName(userRequest.getFirstName());
            response.setLastName(userRequest.getLastName());
            response.setRole(userRequest.getRole());
            response.setActive(userRequest.getActive());

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        try {
            // Update user logic here
            // Find existing user
            // Update fields
            // Save to database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setId(userId);
            response.setUsername(userRequest.getUsername());
            response.setEmail(userRequest.getEmail());
            response.setFirstName(userRequest.getFirstName());
            response.setLastName(userRequest.getLastName());
            response.setRole(userRequest.getRole());
            response.setActive(userRequest.getActive());

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public void deleteUser(Long userId) {
        try {
            // Delete user logic here
            // Find user
            // Delete from database
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

    public List<UserResponse> getAllUsers() {
        try {
            // Get all users logic here
            // Retrieve from database
            // Convert to response list
            return List.of(); // Placeholder
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve users: " + e.getMessage());
        }
    }

    public UserResponse getUserById(Long userId) {
        try {
            // Get user by ID logic here
            // Find in database
            // Convert to response

            UserResponse response = new UserResponse();
            response.setId(userId);
            // Set other fields from database

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve user: " + e.getMessage());
        }
    }

    // ---------------------- Teacher Creation (US N6) ----------------------
    /**
     * Creates a teacher user and assigns optional subjects.
     * Functional style: 1) map request -> entity, 2) save, 3) side-effect assignment & email.
     */
    public com.university.ManageNotes.dto.Response.MessageResponse createTeacher(com.university.ManageNotes.dto.Request.TeacherCreateRequest req) {
        // Validate uniqueness via Optional pipeline
        java.util.Optional<String> duplicationError = java.util.stream.Stream.of(
                        userRepository.existsByUsername(req.getPhone()) ? "Username already exists" : null,
                        userRepository.existsByEmail(req.getEmail()) ? "Email already exists" : null)
                .filter(java.util.Objects::nonNull)
                .findFirst();
        if (duplicationError.isPresent()) {
            return com.university.ManageNotes.dto.Response.MessageResponse.error(duplicationError.get());
        }

        Users teacher = new Users();
        teacher.setUsername(req.getPhone()); // Use phone as login by default
        teacher.setFirstName(req.getFirstName());
        teacher.setLastName(req.getLastName());
        teacher.setPhone(req.getPhone());
        teacher.setDepartment(req.getDepartment());
        teacher.setEmail(req.getEmail());
        teacher.setPassword(passwordEncoder.encode(req.getPassword()));
        teacher.setRole(Role.TEACHER);
        teacher.setActive(true);

        Users saved = userRepository.save(teacher);

        // Assign subjects if provided
        if (!req.getSubjectIds().isEmpty()) {
            // Each subject gets idTeacher mapping; stream for functional style
            subjectRepository.findAllById(req.getSubjectIds()).forEach(s -> {
                s.setIdTeacher(saved.getId());
                subjectRepository.save(s);
            });
        }

        return new com.university.ManageNotes.dto.Response.MessageResponse("Teacher created", "SUCCESS", userMapper.toResponse(saved));
    }

    public void activateUser(Long userId) {
        try {
            // Activate user logic here
            // Find user
            // Set active = true
            // Save to database
        } catch (Exception e) {
            throw new RuntimeException("Failed to activate user: " + e.getMessage());
        }
    }

    public void deactivateUser(Long userId) {
        try {
            // Deactivate user logic here
            // Find user
            // Set active = false
            // Save to database
        } catch (Exception e) {
            throw new RuntimeException("Failed to deactivate user: " + e.getMessage());
        }
    }

    private UserResponse convertToResponse(Users user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        // phone removed
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        return response;
    }

    public UserResponse getCurrentUserResponse() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No authenticated user");
        }
        String username = auth.getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(userMapper::toResponse)
                .toList();
    }
}
