package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.LoginRequest;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.LoginResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.dto.Response.UserProfileResDto;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AppRole;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.security.JwtUtils;
import com.university.ManageNotes.service.AuthService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TeachingLevelRepository teachingLevelRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;

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
    @Transactional
    public MessageResponse register(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new APIException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new APIException("Error: Email is already in use!");
        }

        String roleStr = signupRequest.getRole() != null ? signupRequest.getRole().toUpperCase() : "STUDENT";
        AppRole appRole = AppRole.valueOf(roleStr);
        Roles role = roleRepository.findByAppRole(appRole)
                .orElseThrow(() -> new APIException("Error: Role not found."));

        if (appRole == AppRole.TEACHER) {
            return registerTeacher(signupRequest, role);
        } else if (appRole == AppRole.STUDENT) {
            return registerStudent(signupRequest, role);
        } else {
            return registerAdmin(signupRequest, role);
        }
    }

    private MessageResponse registerStudent(SignupRequest request, Roles role) {
        if (request.getMatricule() == null || request.getMatricule().trim().isEmpty()) {
            throw new APIException("Matricule is required for student registration");
        }
        
        TeachingLevel level;
        if (request.getLevelId() != null) {
            level = teachingLevelRepository.findById(request.getLevelId())
                    .orElseThrow(() -> new ResourceNotFoundException("TeachingLevel", "id", request.getLevelId()));
        } else {
            level = teachingLevelRepository.findAll().stream()
                    .filter(tl -> tl.getStudentLevel() == com.university.ManageNotes.model.enums.StudentLevel.LEVEL1)
                    .findFirst()
                    .orElseThrow(() -> new APIException("Default level LEVEL1 not found in database"));
        }

        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(role);
        student.setMustChangePassword(false);
        student.setIsActive(true);
        student.setMatricule(request.getMatricule());
        student.setSpeciality(request.getSpeciality());
        student.setCycle(request.getCycle());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setPlaceOfBirth(request.getPlaceOfBirth());
        student.setStudentLevel(level);
        
        Student savedStudent = studentRepository.save(student);
        studentRepository.flush();

        return new MessageResponse("Student registered successfully!");
    }

    private MessageResponse registerTeacher(SignupRequest request, Roles role) {
        Teacher teacher = new Teacher();
        teacher.setUsername(request.getUsername());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setRole(role);
        teacher.setMustChangePassword(false);
        teacher.setIsActive(true);
        teacher.setPhoneNumber(request.getPhone());
        
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            teacher.setDepartment(department);
        }
        
        if (request.getLevelIds() != null && !request.getLevelIds().isEmpty()) {
            List<TeachingLevel> levels = new ArrayList<>();
            for (Long levelId : request.getLevelIds()) {
                TeachingLevel level = teachingLevelRepository.findById(levelId)
                        .orElseThrow(() -> new ResourceNotFoundException("TeachingLevel", "id", levelId));
                levels.add(level);
            }
            teacher.setTeachingLevels(levels);
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        teacherRepository.flush();
        
        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            for (Long subjectId : request.getSubjectIds()) {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));
                subject.setTeacher(savedTeacher);
                subjectRepository.save(subject);
            }
        }

        return new MessageResponse("Teacher registered successfully!");
    }

    private MessageResponse registerAdmin(SignupRequest request, Roles role) {
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setMustChangePassword(false);
        user.setIsActive(true);
        userRepository.save(user);
        userRepository.flush();

        return new MessageResponse("Admin registered successfully!");
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

    @Override
    public UserProfileResDto getProfileByUsername(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        
        UserProfileResDto profile = new UserProfileResDto();
        profile.setId(user.getId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setUsername(user.getUsername());
        profile.setRole(user.getRole().getAppRole().name());
        return profile;
    }

    @Override
    @Transactional
    public MessageResponse deleteUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        userRepository.delete(user);
        return new MessageResponse("User deleted successfully!");
    }
}
