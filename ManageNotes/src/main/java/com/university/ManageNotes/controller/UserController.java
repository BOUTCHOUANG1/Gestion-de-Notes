package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.StudentUpdateRequest;
import com.university.ManageNotes.dto.Request.TeacherUpdateRequest;
import com.university.ManageNotes.dto.Request.UpdateCredentialsRequest;
import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.AuthService;
import com.university.ManageNotes.service.SequenceService;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Lookup", description = "Utility endpoints for user/role lookup")
public class UserController {

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    private final AuthService authService;

    private final SubjectRepository subjectRepository;

    private final GradeRepository gradeRepository;

    private final SequenceService sequenceService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public UserProfileResponse me(Authentication authentication) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElseThrow();

        var builder = UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole());

        if (user.getRole() == Role.STUDENT) {
            // For students, find their student record and use students.id
            var student = studentRepository.findByMatricule(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("Student record not found"));
            List<Grades> grades = gradeRepository.findByStudentId(student.getId());
            Map<Long, SubjectResponse> subjectMap = new HashMap<>();
            for (var g : grades) {
                var subj = g.getSubject();
                subjectMap.computeIfAbsent(subj.getId(), k -> {
                    // Get teacher name
                    String teacherName = null;
                    if (subj.getIdTeacher() != null) {
                        var teacher = userRepository.findById(subj.getIdTeacher()).orElse(null);
                        if (teacher != null) {
                            teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                        }
                    }
                    
                    return SubjectResponse.builder()
                            .id(subj.getId())
                            .code(subj.getCode())
                            .name(subj.getName())
                            .credits(subj.getCredits())
                            .description(subj.getDescription())
                            .active(subj.getActive())
                            .level(subj.getLevel())
                            .cycle(subj.getCycle())
                            .semesterId(g.getSemesters() != null ? g.getSemesters().getId() : null)
                            .semesterName(g.getSemesters() != null ? g.getSemesters().getName() : null)
                            .departmentId(subj.getDepartment() != null ? subj.getDepartment().getId() : null)
                            .departmentName(subj.getDepartment() != null ? subj.getDepartment().getName() : null)
                            .teacherId(subj.getIdTeacher())
                            .teacherName(teacherName)
                            .build();
                });
            }
            builder.subjects(new ArrayList<>(subjectMap.values()));
        } else if (user.getRole() == Role.TEACHER) {
            var subjects = subjectRepository.findByIdTeacher(user.getId());
            
            // Add subjects to response with proper semester and department info
            List<SubjectResponse> subjectResponses = subjects.stream()
                    .map(sub -> {
                        return SubjectResponse.builder()
                                .id(sub.getId())
                                .code(sub.getCode())
                                .name(sub.getName())
                                .credits(sub.getCredits())
                                .description(sub.getDescription())
                                .active(sub.getActive())
                                .level(sub.getLevel())
                                .cycle(sub.getCycle())
                                .semesterId(sub.getSemester() != null ? sub.getSemester().getId() : null)
                                .semesterName(sub.getSemester() != null ? sub.getSemester().getName() : null)
                                .departmentId(sub.getDepartment() != null ? sub.getDepartment().getId() : null)
                                .departmentName(sub.getDepartment() != null ? sub.getDepartment().getName() : null)
                                .teacherId(sub.getIdTeacher())
                                .teacherName(user.getFirstName() + " " + user.getLastName())
                                .build();
                    })
                    .collect(Collectors.toList());
            builder.subjects(subjectResponses);
            
            // Set teacher levels from subjects they teach
            List<String> teacherLevels = subjects.stream()
                    .map(sub -> sub.getLevel() != null ? sub.getLevel().name() : null)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            builder.levels(teacherLevels);
        }

        return builder.build();
    }

    @PostMapping("/me/credentials")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public MessageResponse updateCredentials(@AuthenticationPrincipal UserPrincipal principal,
                                             @Valid @RequestBody UpdateCredentialsRequest request) {
        if (request.getNewPassword() != null && !request.getNewPassword().equals(request.getConfirmPassword())) {
            return MessageResponse.error("Passwords do not match");
        }
        return authService.updateCredentials(principal.getId(), request.getCurrentPassword(), request.getNewUsername(), request.getNewPassword());
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    @Operation(summary = "List students visible to current user")
    public List<UserProfileResponse> students(@AuthenticationPrincipal UserPrincipal principal) {
        List<Students> students;
        if (principal.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            students = studentRepository.findAll();
        } else {
            students = studentRepository.findStudentsByTeacherSubject(principal.getId());
        }
        return students.stream()
                .sorted((s1, s2) -> s2.getCreatedDate().compareTo(s1.getCreatedDate()))
                .map(s -> {
            // Find corresponding user to get proper ID
            var user = userRepository.findByUsername(s.getMatricule()).orElse(null);
            
            // Get student's grades and subjects using students.id
            List<SubjectResponse> studentSubjects = new ArrayList<>();
            if (user != null) {
                List<Grades> grades = gradeRepository.findByStudentId(s.getId());
                Map<Long, SubjectResponse> subjectMap = new HashMap<>();
                for (var g : grades) {
                    var subj = g.getSubject();
                    subjectMap.computeIfAbsent(subj.getId(), k -> {
                        // Get teacher name inside the lambda
                        String teacherName = null;
                        if (subj.getIdTeacher() != null) {
                            var teacher = userRepository.findById(subj.getIdTeacher()).orElse(null);
                            if (teacher != null) {
                                teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                            }
                        }
                        
                        return SubjectResponse.builder()
                                .id(subj.getId())
                                .code(subj.getCode())
                                .name(subj.getName())
                                .credits(subj.getCredits())
                                .description(subj.getDescription())
                                .active(subj.getActive())
                                .level(subj.getLevel())
                                .cycle(subj.getCycle())
                                .semesterId(g.getSemesters() != null ? g.getSemesters().getId() : null)
                                .semesterName(g.getSemesters() != null ? g.getSemesters().getName() : null)
                                .departmentId(subj.getDepartment() != null ? subj.getDepartment().getId() : null)
                                .departmentName(subj.getDepartment() != null ? subj.getDepartment().getName() : null)
                                .teacherId(subj.getIdTeacher())
                                .teacherName(teacherName)
                                .build();
                    });
                }
                studentSubjects = new ArrayList<>(subjectMap.values());
            }
            
            return UserProfileResponse.builder()
                    .id(user != null ? user.getId() : s.getId()) // Use user ID, not student record ID
                    .username(s.getMatricule())
                    .firstName(s.getFirstName())
                    .lastName(s.getLastName())
                    .email(s.getEmail())
                    .role(Role.STUDENT)
                    .level(s.getLevel())
                    .subjects(studentSubjects)
                    .build();
        }).collect(Collectors.toList());
    }

    @GetMapping("/students/level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List students by level (Admin only)")
    public List<UserProfileResponse> studentsByLevel(@PathVariable String level) {
        return Optional.ofNullable(level)
                .map(String::toUpperCase)
                .flatMap(lv -> Arrays.stream(StudentLevel.values())
                        .filter(sl -> sl.name().equals("LEVEL" + lv.replace("L", "")))
                        .findFirst())
                .map(studentRepository::findByLevel)
                .orElseGet(Collections::emptyList)
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
    public ResponseEntity<List<UserProfileResponse>> teachers() {
        List<UserProfileResponse> teachers = userRepository.findByRole(Role.TEACHER)
                .stream()
                .map(u -> UserProfileResponse.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .role(u.getRole())
                        .build())
                .toList();

        // enrich each teacher with subjects
        teachers.forEach(t -> {
            var subjects = subjectRepository.findByIdTeacher(t.getId());
            
            // Add subjects to teacher with proper semester and department info
            List<SubjectResponse> teacherSubjects = subjects.stream()
                    .map(sub -> {
                        return SubjectResponse.builder()
                                .id(sub.getId())
                                .code(sub.getCode())
                                .name(sub.getName())
                                .credits(sub.getCredits())
                                .description(sub.getDescription())
                                .active(sub.getActive())
                                .level(sub.getLevel())
                                .cycle(sub.getCycle())
                                .semesterId(sub.getSemester() != null ? sub.getSemester().getId() : null)
                                .semesterName(sub.getSemester() != null ? sub.getSemester().getName() : null)
                                .departmentId(sub.getDepartment() != null ? sub.getDepartment().getId() : null)
                                .departmentName(sub.getDepartment() != null ? sub.getDepartment().getName() : null)
                                .teacherId(sub.getIdTeacher())
                                .teacherName(t.getFirstName() + " " + t.getLastName())
                                .build();
                    })
                    .collect(Collectors.toList());
            t.setSubjects(teacherSubjects);
            
            // Set teacher levels from subjects they teach
            List<String> teacherLevels = subjects.stream()
                    .map(sub -> sub.getLevel() != null ? sub.getLevel().name() : null)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            t.setLevels(teacherLevels);
        });

        ResponseEntity<List<UserProfileResponse>> resp = Optional.of(teachers)
                .filter(list -> !list.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());

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
    @Transactional
    public MessageResponse deleteTeacher(@PathVariable Long id) {
        return userRepository.findById(id)
                .filter(u -> u.getRole() == Role.TEACHER)
                .map(teacher -> {
                    // Prevent deleting default system users
                    if (("admin".equals(teacher.getUsername()) && teacher.getRole() == Role.ADMIN) ||
                        ("teacher".equals(teacher.getUsername()) && teacher.getRole() == Role.TEACHER)) {
                        return MessageResponse.error("Cannot delete default system users (admin/teacher)");
                    }

                    // Preserve grades by setting enteredBy to null (can be reassigned)
                    List<Grades> teacherGrades = gradeRepository.findByEnteredById(teacher.getId());
                    teacherGrades.forEach(grade -> grade.setEnteredBy(null));
                    gradeRepository.saveAll(teacherGrades);

                    // Preserve subjects by removing teacher assignment (can be reassigned)
                    List<Subject> teacherSubjects = subjectRepository.findByIdTeacher(teacher.getId());
                    teacherSubjects.forEach(subject -> subject.setIdTeacher(null));
                    subjectRepository.saveAll(teacherSubjects);

                    // Delete user levels
                    if (teacher.getLevels() != null) {
                        teacher.getLevels().clear();
                    }

                    // Delete the teacher
                    userRepository.delete(teacher);
                    userRepository.flush();
                    sequenceService.resetUserSequence();

                    return MessageResponse.success("Teacher deleted successfully. " + 
                            teacherGrades.size() + " grades and " + teacherSubjects.size() + " subjects are now unassigned and can be reassigned to other teachers.");
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
    @Transactional
    public MessageResponse deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    // Prevent deleting default system users
                    if (("admin".equals(user.getUsername()) && user.getRole() == Role.ADMIN) ||
                        ("teacher".equals(user.getUsername()) && user.getRole() == Role.TEACHER)) {
                        return MessageResponse.error("Cannot delete default system users (admin/teacher)");
                    }

                    int deletedItems = 0;
                    StringBuilder details = new StringBuilder();

                    if (user.getRole() == Role.STUDENT) {
                        // Delete student grades
                        List<Grades> studentGrades = gradeRepository.findByStudentId(user.getId());
                        gradeRepository.deleteAll(studentGrades);
                        deletedItems += studentGrades.size();
                        details.append(studentGrades.size()).append(" grades, ");

                        // Delete student record
                        studentRepository.findByEmail(user.getEmail())
                                .ifPresent(student -> {
                                    studentRepository.delete(student);
                                    sequenceService.resetStudentSequence();
                                });
                        details.append("student record, ");
                    } else if (user.getRole() == Role.TEACHER) {
                        // Preserve grades by removing teacher assignment (can be reassigned)
                        List<Grades> teacherGrades = gradeRepository.findByEnteredById(user.getId());
                        teacherGrades.forEach(grade -> grade.setEnteredBy(null));
                        gradeRepository.saveAll(teacherGrades);
                        deletedItems += teacherGrades.size();
                        details.append(teacherGrades.size()).append(" grades unassigned, ");

                        // Preserve subjects by removing teacher assignment (can be reassigned)
                        List<Subject> teacherSubjects = subjectRepository.findByIdTeacher(user.getId());
                        teacherSubjects.forEach(subject -> subject.setIdTeacher(null));
                        subjectRepository.saveAll(teacherSubjects);
                        deletedItems += teacherSubjects.size();
                        details.append(teacherSubjects.size()).append(" subjects unassigned, ");

                        // Clear user levels
                        if (user.getLevels() != null) {
                            user.getLevels().clear();
                        }
                    }

                    // Delete the user
                    userRepository.delete(user);
                    userRepository.flush();
                    sequenceService.resetUserSequence();

                    String message = user.getRole() == Role.STUDENT ? 
                        "Student and all related data deleted successfully." : 
                        "User deleted successfully.";
                    if (deletedItems > 0) {
                        String action = user.getRole() == Role.STUDENT ? "Removed: " : "Unassigned: ";
                        message += " " + action + details.toString().replaceAll(", $", "");
                    }
                    return MessageResponse.success(message);
                })
                .orElse(MessageResponse.error("User not found"));
    }
}
