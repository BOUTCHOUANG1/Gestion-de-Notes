package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.JwtResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.mapper.UserMapper;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.JwtUtils;
import com.university.ManageNotes.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final UserDetailsService userDetailsService;

    public MessageResponse registerUser(SignupRequest signupRequest) {
        // Functional attempt reverted due to type-safety issues; keeping imperative flow with
        // clear comments for readability and future refactor.

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return MessageResponse.error("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return MessageResponse.error("Error: Email is already in use!");
        }

        Role requestedRole = signupRequest.getRole();
        if (requestedRole == null) {
            requestedRole = Role.STUDENT;
            signupRequest.setRole(requestedRole);
        }

        // Generate default password if not provided (admin creating users)
        String password = signupRequest.getPassword();
        if (password == null || password.isBlank()) {
            password = "password123"; // Default password
            signupRequest.setPassword(password);
        }

        // No registrationKey handling; admins directly register any role

        Users user = userMapper.toEntity(signupRequest);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);

        // Handle role-specific fields
        if (requestedRole == Role.TEACHER) {
            user.setLevels(signupRequest.getLevels());
            user.setDepartment(signupRequest.getDepartment());
            user.setPhone(signupRequest.getPhone());
        } else {
            // Clear teacher fields for non-teacher roles
            user.setLevels(null);
            user.setDepartment(null);
            user.setPhone(null);
        }

        Users saved = userRepository.save(user);

        if (saved.getRole() == Role.STUDENT) {
            Students student = new Students();
            student.setFirstName(saved.getFirstName());
            student.setLastName(saved.getLastName());
            student.setEmail(saved.getEmail());
            student.setMatricule(signupRequest.getMatricule() != null ? signupRequest.getMatricule() : UUID.randomUUID().toString());
            student.setLevel(signupRequest.getLevel());
            student.setCycle(signupRequest.getCycle());
            student.setSpeciality(signupRequest.getSpeciality());
            student.setPlaceOfBirth(signupRequest.getPlaceOfBirth());
            student.setDateOfBirth(signupRequest.getDateOfBirth());
            studentRepository.save(student);
        }

        if (saved.getRole() == Role.STUDENT) {
            Students st = studentRepository.findByEmail(saved.getEmail()).orElse(null);
            if (st != null && !saved.getUsername().equals(st.getMatricule())) {
                saved.setUsername(st.getMatricule());
                userRepository.save(saved);
            }
        }

        try {
            emailService.sendSimpleMessage(
                    saved.getEmail(),
                    "Your account credentials",
                    "Hello " + saved.getFirstName() + ",\n\n" +
                            "You can now access the Faculty of Science of Yaounde 1 platform.\n" +
                            "Username: " + saved.getUsername() + "\n" +
                            "Password: " + signupRequest.getPassword() + "\n" +
                            "Please change these credentials after your first login.\n\nRegards");
        } catch (Exception ignored) {}

        // Create role-specific response
        UserResponse response = userMapper.toResponse(saved);
        if (saved.getRole() == Role.STUDENT) {
            // Clear teacher fields and populate student fields
            response.setLevels(null);
            response.setDepartment(null);
            response.setPhone(null);
            
            // Populate student fields from SignupRequest
            response.setLevel(signupRequest.getLevel() != null ? signupRequest.getLevel().name() : null);
            response.setMatricule(signupRequest.getMatricule());
            response.setSpeciality(signupRequest.getSpeciality());
            response.setCycle(signupRequest.getCycle() != null ? signupRequest.getCycle().name() : null);
            response.setDateOfBirth(signupRequest.getDateOfBirth());
            response.setPlaceOfBirth(signupRequest.getPlaceOfBirth());
        }

        return new MessageResponse(
                "User registered successfully!",
                "SUCCESS",
                response
        );
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Object principal = SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Users userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(jwt);
            jwtResponse.setType("Bearer");
            jwtResponse.setId(userDetails.getId());
            jwtResponse.setUsername(userDetails.getUsername());
            jwtResponse.setEmail(userDetails.getEmail());
            jwtResponse.setFirstName(userDetails.getFirstName());
            jwtResponse.setLastName(userDetails.getLastName());
            jwtResponse.setRole(userDetails.getRole());
            jwtResponse.setAuthorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            jwtResponse.setMustChangePassword(userDetails.getMustChangePassword());
            
            // Role-specific data population
            if (userDetails.getRole() == Role.ADMIN) {
                jwtResponse.setMustChooseDepartment(true);
                jwtResponse.setDepartments(departmentRepository.findAll()
                        .stream()
                        .map(Department::getName)
                        .toList());
            } else if (userDetails.getRole() == Role.TEACHER) {
                jwtResponse.setMustChooseDepartment(false);
                jwtResponse.setDepartments(null); // Remove this field
                // Add teacher-specific data
                jwtResponse.setDepartment(userDetails.getDepartment());
                jwtResponse.setLevels(userDetails.getLevels());
                jwtResponse.setPhone(userDetails.getPhone());
                
                // Add subjects taught by this teacher
                var subjects = subjectRepository.findByIdTeacher(userDetails.getId());
                jwtResponse.setSubjects(subjects.stream()
                        .map(subject -> subject.getName() + " (" + subject.getCode() + ")")
                        .toList());
            } else {
                jwtResponse.setMustChooseDepartment(false);
                jwtResponse.setDepartments(null);
            }
            return jwtResponse;
        } else {
            throw new RuntimeException("Error: User not authenticated.");
        }
    }

    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        } else {
            throw new RuntimeException("Error: User not authenticated.");
        }
    }

    public MessageResponse changePassword(String username, String newPassword) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);

        return MessageResponse.success("Password changed successfully!");
    }

    public MessageResponse resetPassword(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User with email not found."));

        // generate a random temporary password (8 alphanumeric chars)
        String tempPassword = java.util.UUID.randomUUID().toString()
                .replaceAll("[^A-Za-z0-9]", "")
                .substring(0, 8);

        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        // send email
        try {
            emailService.sendSimpleMessage(
                    email,
                    "Your temporary password",
                    "Hello " + user.getFirstName() + ",\n\nYour new temporary password is: " + tempPassword +
                            "\nPlease log in and change it immediately.\n\nRegards");
        } catch (Exception ignored) {}

        return new MessageResponse("Temporary password sent to email", "SUCCESS", tempPassword);
    }

    public MessageResponse updateCredentials(Long userId, String currentPassword, String newUsername, String newPassword) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return MessageResponse.error("Incorrect current password");
        }
        boolean changed = false;
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            if (userRepository.existsByUsername(newUsername)) {
                return MessageResponse.error("Username already taken");
            }
            user.setUsername(newUsername);
            changed = true;
        }
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
            changed = true;
        }
        if (changed) {
            user.setMustChangePassword(false);
        }

        userRepository.save(user);
        return MessageResponse.success("Credentials updated");
    }

    public UserPrincipal getUserPrincipalByUsername(String username) {
        return (UserPrincipal) userDetailsService.loadUserByUsername(username);
    }
}
