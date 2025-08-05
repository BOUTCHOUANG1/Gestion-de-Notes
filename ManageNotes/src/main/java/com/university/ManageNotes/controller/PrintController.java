package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.dto.Response.UserProfileResponse;
import com.university.ManageNotes.model.ProcessVerbalType;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/print")
@RequiredArgsConstructor
public class PrintController {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

    @GetMapping("/level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchPrintData(@PathVariable String level,
                                            @RequestParam("type") String type,
                                            @RequestParam(required = false) Long semesterId) {
        var lvlOpt = java.util.Arrays.stream(StudentLevel.values())
                .filter(l -> l.name().equals("LEVEL" + level.replace("L", "")))
                .findFirst();
        var pvOpt = ProcessVerbalType.fromLabel(type);
        if (lvlOpt.isEmpty() || pvOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid level or type");
        }
        var students = studentRepository.findByLevel(lvlOpt.get());

        return switch (pvOpt.get()) {
            case STUDENT_LIST -> {
                java.util.List<UserProfileResponse> list = students.stream().map(s -> UserProfileResponse.builder()
                        .id(s.getId())
                        .username(s.getMatricule())
                        .firstName(s.getFirstName())
                        .lastName(s.getLastName())
                        .email(s.getEmail())
                        .role(com.university.ManageNotes.model.Role.STUDENT)
                        .build()).toList();
                yield ResponseEntity.ok(list);
            }
            case GRADE_CC1, GRADE_CC2, GRADE_SN1, GRADE_SN2, YEAR_SUMMARY -> {
                String label = switch (pvOpt.get()) {
                    case GRADE_CC1 -> "CC1";
                    case GRADE_CC2 -> "CC2";
                    case GRADE_SN1 -> "SN1";
                    case GRADE_SN2 -> "SN2";
                    default -> null; // YEAR_SUMMARY
                };
                java.util.List<StudentGradesResponse> rows = students.stream()
                        .map(s -> gradeService.getStudentGrades(s.getId(), semesterId))
                        .map(resp -> {
                            if (label != null) {
                                var filtered = resp.getGrades().stream()
                                        .filter(g -> label.equalsIgnoreCase(g.getPeriodLabel()))
                                        .toList();
                                resp.setGrades(filtered);
                            }
                            return resp;
                        }).toList();
                yield ResponseEntity.ok(rows);
            }
        };
    }
}
