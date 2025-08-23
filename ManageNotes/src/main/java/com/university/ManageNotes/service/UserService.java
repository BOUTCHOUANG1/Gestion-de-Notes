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
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setRole(userRequest.getRole());
        user.setActive(userRequest.getActive());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setMustChangePassword(false);

        Users saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUsername().equals(userRequest.getUsername()) && 
            userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (!user.getEmail().equals(userRequest.getEmail()) && 
            userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setRole(userRequest.getRole());
        user.setActive(userRequest.getActive());
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        Users updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
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
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    public void deactivateUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
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
