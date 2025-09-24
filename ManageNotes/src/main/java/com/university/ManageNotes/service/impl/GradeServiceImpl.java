package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.GradeService;
import com.university.ManageNotes.service.RevendicationPeriodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final RevendicationPeriodService revendicationPeriodService;
    private final ModelMapper modelMapper;

    @Override
    public GradeRequest createGrade(GradeRequest gradeRequest) {
       Grades grade = modelMapper.map(gradeRequest, Grades.class);

       List<Grades> gradeByTeacherDb = gradeRepository.findByTeacherAndStudentAndTeachingLevelAndSemester(
               grade.getExaminer().getId(),
               grade.getStudent().getId(), grade.getStudent().getStudentLevel(), grade.getSemester());

       if(gradeByTeacherDb != null && !gradeByTeacherDb.isEmpty()){
           throw new APIException("Grade already exists for this teacher with name " + grade.getExaminer().getUsername());
       }
           throw new APIException("No grade found for this teacher with name " + grade.getExaminer().getUsername());
       }

       List<Grades> gradeDb = gradeRepository.findAll();

    gradeDb.stream()
    }

    public StudentGradesResponse getStudentGrades(Long userId, Long semesterId) {
        // Convert user ID to student ID for grade lookup
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"STUDENT".equals(user.getAppRole().name())) {
            throw new RuntimeException("User is not a student");
        }

        var student = studentRepository.findByMatricule(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Student record not found"));

        List<Grades> grades;
        if (semesterId != null) {
            grades = gradeRepository.findByStudentIdAndSemesterId(student.getId(), semesterId);
        } else {
            grades = gradeRepository.findByStudentId(student.getId());
        }

        List<GradeResponse> gradeResponses = grades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(userId); // Return user ID for consistency
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        if (semesterId != null) {
            var semOpt = semesterRepository.findById(semesterId);
            semOpt.ifPresent(se -> {
                response.setSemesterId(se.getId());
                response.setSemesterName(se.getName());
            });
        } else if (!grades.isEmpty()) {
            // Use semester from first grade if no specific semester requested
            var firstGrade = grades.get(0);
            if (firstGrade.getSemesters() != null) {
                response.setSemesterId(firstGrade.getSemesters().getId());
                response.setSemesterName(firstGrade.getSemesters().getName());
            }
        }
        response.setGrades(gradeResponses);

        // simple GPA calculation
        if (!grades.isEmpty()) {
            double avg = grades.stream()
                    .mapToDouble(Grades::getScore)
                    .average().orElse(0);
            response.setGpa(Math.round(avg * 100.0) / 100.0);
        }

        // Build SubjectResponse list for frontend with complete data
        Map<String, SubjectResponse> subjectMap = new HashMap<>();
        for (var gr : gradeResponses) {
            subjectMap.computeIfAbsent(gr.getSubjectCode(), k -> {
                var subj = subjectRepository.findById(gr.getSubjectId()).orElse(null);
                String teacherName = null;
                if (subj != null && subj.getIdTeacher() != null) {
                    var teacher = userRepository.findById(subj.getIdTeacher()).orElse(null);
                    if (teacher != null) {
                        teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                    }
                }

                return SubjectResponse.builder()
                        .id(subj != null ? subj.getId() : null)
                        .code(gr.getSubjectCode())
                        .name(gr.getSubjectName())
                        .credits(subj != null ? subj.getCredits() : BigDecimal.ZERO)
                        .description(subj != null ? subj.getDescription() : null)
                        .active(subj != null ? subj.getActive() : null)
                        .level(subj != null ? subj.getLevel() : null)
                        .cycle(subj != null ? subj.getCycle() : null)
                        .semesterId(gr.getSemesterId())
                        .semesterName(gr.getSemesterName())
                        .departmentId(subj != null && subj.getDepartment() != null ? subj.getDepartment().getId() : null)
                        .departmentName(subj != null && subj.getDepartment() != null ? subj.getDepartment().getName() : null)
                        .teacherId(subj != null ? subj.getIdTeacher() : null)
                        .teacherName(teacherName)
                        .build();
            });
        }

        List<SubjectResponse> subjectList = new ArrayList<>(subjectMap.values());
        response.setSubjects(subjectList);
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setUsername(student.getMatricule());
        if(student.getLevel()!=null) response.setLevel(student.getLevel().name());
        response.setRole("STUDENT");

        return response;
    }

    public List<GradeResponse> getTeacherGrades() {
        Long teacherId = null;
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
                teacherId = up.getId();
            }
        } catch (Exception ignored) {}

        if (teacherId == null) {
            return List.of();
        }

        List<Grades> grades = gradeRepository.findByEnteredById(teacherId);
        return grades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GradeRequest updateGrade(Long gradeId, GradeRequest gradeRequest) {
        Grades grade = modelMapper.map(gradeRequest, Grades.class);

        Grades gradeDb = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Grades", "gradeId", gradeId));

        if (grade.getScore() != null) {
            gradeDb.setScore(grade.getScore());
        }

        if (grade.getComments() != null) {
            gradeDb.setComments(grade.getComments());
        }

        if (grade.getExam() != null) {
            gradeDb.setExam(grade.getExam());
        }

        Grades updatedGrade = gradeRepository.save(gradeDb);
        return convertToResponse(updatedGrade);
    }
}
