package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentUpdateRequest;
import com.university.ManageNotes.dto.Request.TeacherUpdateRequest;
import com.university.ManageNotes.dto.Request.UpdateCredentialsRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Lookup", description = "Utility endpoints for user/role lookup")
public class UserController {

    private final UserRepository userRepository;

    private final com.university.ManageNotes.repository.StudentRepository studentRepository;

    private final AuthService authService;

    private final SubjectRepository subjectRepository;

    private final com.university.ManageNotes.repository.GradeRepository gradeRepository;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public com.university.ManageNotes.dto.Response.UserProfileResponse me(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElseThrow();

        var builder = com.university.ManageNotes.dto.Response.UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole());

        if (user.getRole() == com.university.ManageNotes.model.Role.STUDENT) {
            java.util.List<com.university.ManageNotes.model.Grades> grades = gradeRepository.findByStudentId(user.getId());
            java.util.Map<Long, com.university.ManageNotes.dto.Response.TopicDto.TopicDtoBuilder> map = new java.util.HashMap<>();
            for (var g : grades) {
                var subj = g.getSubject();
                var b = map.computeIfAbsent(subj.getId(), k -> com.university.ManageNotes.dto.Response.TopicDto.builder()
                        .code(subj.getCode())
                        .title(subj.getName())
                        .semester(g.getSemesters()!=null?g.getSemesters().getName().toLowerCase():null));
                if (g.getPeriodLabel()!=null) {
                    String pl = g.getPeriodLabel().toUpperCase();
                    if (pl.startsWith("CC")) b.cc(g.getValue());
                    else if (pl.startsWith("SN")) b.sn(g.getValue());
                }
                if (b.build().getCredit()==null && g.getSubject().getCredits()!=null) {
                    b.credit(g.getSubject().getCredits());
                }
            }
            builder.topics(map.values().stream().map(com.university.ManageNotes.dto.Response.TopicDto.TopicDtoBuilder::build).toList());
        } else if (user.getRole() == com.university.ManageNotes.model.Role.TEACHER) {
            var subjects = subjectRepository.findByIdTeacher(user.getId());
            java.util.Map<String, java.util.List<com.university.ManageNotes.dto.Response.TeacherSubjectDto.SubjectInfo>> map = new java.util.HashMap<>();
            for (var sub : subjects) {
                String level = sub.getLevel()!=null?sub.getLevel().name().replace("LEVEL","L"):"";
                map.computeIfAbsent(level, k->new java.util.ArrayList<>())
                        .add(com.university.ManageNotes.dto.Response.TeacherSubjectDto.SubjectInfo.builder()
                                .code(sub.getCode())
                                .title(sub.getName())
                                .build());
            }
            var levels = map.entrySet().stream().map(e-> com.university.ManageNotes.dto.Response.TeacherSubjectDto.builder()
                    .level(e.getKey())
                    .subjects(e.getValue())
                    .build()).toList();
            builder.levels(levels);
        }

        return builder.build();
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

    @GetMapping("/students/level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List students by level (Admin only)")
    public java.util.List<UserProfileResponse> studentsByLevel(@PathVariable String level) {
        return java.util.Optional.ofNullable(level)
                .map(String::toUpperCase)
                .flatMap(lv -> java.util.Arrays.stream(StudentLevel.values())
                        .filter(sl -> sl.name().equals("LEVEL" + lv.replace("L", "")))
                        .findFirst())
                .map(studentRepository::findByLevel)
                .orElseGet(java.util.Collections::emptyList)
                .stream()
                .map(s -> UserProfileResponse.builder()
                        .id(s.getId())
                        .username(s.getMatricule())
                        .firstName(s.getFirstName())
                        .lastName(s.getLastName())
                        .email(s.getEmail())
                        .role(Role.STUDENT)
                        .build())
                .toList();
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all teachers (Admin only)")
    public org.springframework.http.ResponseEntity<java.util.List<UserProfileResponse>> teachers() {
        java.util.List<UserProfileResponse> teachers = userRepository.findByRole(Role.TEACHER).stream()
                .map(u -> UserProfileResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .build())
                .toList();

        // enrich each teacher with levels taught
        teachers.forEach(t -> {
            var subjects = subjectRepository.findByIdTeacher(t.getId());
            java.util.Map<String, java.util.List<com.university.ManageNotes.dto.Response.TeacherSubjectDto.SubjectInfo>> map = new java.util.HashMap<>();
            for (var sub : subjects) {
                String level = sub.getLevel()!=null?sub.getLevel().name().replace("LEVEL","L"):"";
                var list = map.computeIfAbsent(level, k->new java.util.ArrayList<>());
                list.add(com.university.ManageNotes.dto.Response.TeacherSubjectDto.SubjectInfo.builder()
                        .code(sub.getCode())
                        .title(sub.getName())
                        .build());
            }
            java.util.List<com.university.ManageNotes.dto.Response.TeacherSubjectDto> levels = map.entrySet().stream()
                    .map(e -> com.university.ManageNotes.dto.Response.TeacherSubjectDto.builder()
                            .level(e.getKey())
                            .subjects(e.getValue())
                            .build())
                    .toList();
            t.setLevels(levels);
        });

        org.springframework.http.ResponseEntity<java.util.List<UserProfileResponse>> resp = java.util.Optional.of(teachers)
                .filter(list -> !list.isEmpty())
                .map(org.springframework.http.ResponseEntity::ok)
                .orElseGet(() -> org.springframework.http.ResponseEntity.noContent().build());

        return resp;
    }

    /**
     * Delete a teacher by id.
     * <p>
     * Functional-style implementation uses Optional mapping to keep the control-flow declarative
     * and side-effect free as much as possible. The method will:
     *  1. Fetch the user and filter to TEACHER role.
     *  2. For all subjects linked to the teacher, remove the assignment (idTeacher ← null).
     *  3. Delete the teacher record.
     *  4. Return a SUCCESS MessageResponse that includes a hint for the UI (AC2) when at least one
     *     subject became unassigned.
     * If the user is not a teacher or doesn't exist, we short-circuit with an ERROR response.
     */
    @DeleteMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a teacher by ID (Admin only)")
    public MessageResponse deleteTeacher(@PathVariable Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.TEACHER)
                .map(teacher -> {
                    // All subjects currently linked to this teacher.
                    java.util.List<com.university.ManageNotes.model.Subject> orphanedSubjects = subjectRepository.findByIdTeacher(teacher.getId());

                    // Detach teacher from subjects (pure side-effect kept minimal & transactional by Spring).
                    orphanedSubjects.forEach(sub -> sub.setIdTeacher(null));
                    subjectRepository.saveAll(orphanedSubjects);

                    userRepository.delete(teacher);

                    String msgSuffix = orphanedSubjects.isEmpty() ? "" : " Note: there are now " + orphanedSubjects.size() + " subject(s) without an assigned teacher.";
                    return MessageResponse.success("Teacher deleted successfully." + msgSuffix);
                })
                .orElse(MessageResponse.error("Teacher not found or not a teacher"));
    }

    /**
     * Activate or deactivate a student account.
     * Functional approach: Optional pipeline ensures declarative flow without imperative branching.
     */
    @PatchMapping("/students/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageResponse setStudentActive(@PathVariable Long id, @RequestParam boolean active) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .map(u -> {
                    u.setActive(active);
                    userRepository.save(u);
                    String state = active ? "activated" : "deactivated";
                    return MessageResponse.success("Student account " + state + " successfully");
                })
                .orElse(MessageResponse.error("Student not found"));
    }

    @PutMapping("/teachers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update teacher information (Admin only)")
    @Transactional
    public ResponseEntity<UserProfileResponse> updateTeacher(
            @PathVariable Long id, 
            @RequestBody TeacherUpdateRequest request) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.TEACHER)
                .map(teacher -> {
                    teacher.setFirstName(request.getFirstName());
                    teacher.setLastName(request.getLastName());
                    teacher.setEmail(request.getEmail());
                    teacher.setPhone(request.getPhone());
                    teacher.setDepartment(request.getDepartment());
                    teacher.setLevels(request.getLevels());
                    userRepository.save(teacher);
                    
                    return ResponseEntity.ok(UserProfileResponse.builder()
                            .id(teacher.getId())
                            .username(teacher.getUsername())
                            .firstName(teacher.getFirstName())
                            .lastName(teacher.getLastName())
                            .email(teacher.getEmail())
                            .role(teacher.getRole())
                            .build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update student information (Admin only)")
    @Transactional
    public ResponseEntity<UserProfileResponse> updateStudent(
            @PathVariable Long id, 
            @RequestBody StudentUpdateRequest request) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .map(user -> {
                    // Update Users entity
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    userRepository.save(user);
                    
                    // Update Students entity
                    studentRepository.findByEmail(user.getEmail())
                            .ifPresent(student -> {
                                student.setFirstName(request.getFirstName());
                                student.setLastName(request.getLastName());
                                student.setEmail(request.getEmail());
                                student.setMatricule(request.getMatricule());
                                student.setLevel(StudentLevel.valueOf(request.getLevel()
                                        .toUpperCase()));
                                student.setSpeciality(request.getSpeciality());
                                if (request.getCycle() != null) {
                                    student.setCycle(StudentCycle.valueOf(request.getCycle().toUpperCase()));
                                }
                                studentRepository.save(student);
                            });
                    
                    return ResponseEntity.ok(UserProfileResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .role(Role.STUDENT)
                            .build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user by ID (Admin only)")
    public MessageResponse deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    if (user.getRole() == Role.STUDENT) {
                        studentRepository.findByEmail(user.getEmail()).ifPresent(studentRepository::delete);
                    }
                    userRepository.delete(user);
                    return MessageResponse.success("User deleted successfully");
                })
                .orElse(MessageResponse.error("User not found"));
    }
}
