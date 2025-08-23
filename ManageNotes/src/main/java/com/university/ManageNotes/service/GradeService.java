package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.ReportResponse;
import com.university.ManageNotes.dto.Response.StudentGradesResponse;
import com.university.ManageNotes.model.Subject;
import java.math.BigDecimal;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.GradingWindowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final GradingWindowService gradingWindowService;

    @Autowired
    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, UserRepository userRepository, SemesterRepository semesterRepository, GradingWindowService gradingWindowService) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.semesterRepository = semesterRepository;
        this.gradingWindowService = gradingWindowService;
    }

    public MessageResponse addGrade(GradeRequest gradeRequest) {
        try {
            Grades grade = new Grades();
            grade.setStudent(studentRepository.findById(gradeRequest.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found")));
            grade.setSubject(subjectRepository.findById(gradeRequest.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found")));
            grade.setSemesters(semesterRepository.findById(gradeRequest.getSemesterId())
                    .orElseThrow(() -> new RuntimeException("Semester not found")));
            grade.setValue(gradeRequest.getValue());
            grade.setMaxValue(gradeRequest.getMaxValue() != null ? gradeRequest.getMaxValue() : 20.0);
            grade.setType(gradeRequest.getType());
            grade.setPeriodLabel(gradeRequest.getPeriodLabel());
            grade.setComments(gradeRequest.getComments());
            grade.setEnteredBy(userRepository.findById(gradeRequest.getEnteredBy())
                    .orElseThrow(() -> new RuntimeException("User not found")));
            gradeRepository.save(grade);
            return MessageResponse.success("Grade added successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to add grade: " + e.getMessage());
        }
    }

    public GradeResponse updateGrade(Long gradeId, GradeUpdateRequest gradeRequest) {
        Grades grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (gradeRequest.getValue() != null) {
            grade.setValue(gradeRequest.getValue());
        }

        if (gradeRequest.getComments() != null) {
            grade.setComments(gradeRequest.getComments());
        }
        if (gradeRequest.getType() != null) {
            grade.setType(gradeRequest.getType());
        }

        Grades updatedGrade = gradeRepository.save(grade);
        return convertToResponse(updatedGrade);
    }

    public MessageResponse deleteGrade(Long id) {
        try {
            gradeRepository.deleteById(id);
            return MessageResponse.success("Grade deleted successfully!");
        } catch (Exception e) {
            return MessageResponse.error("Failed to delete grade: " + e.getMessage());
        }
    }

    public ReportResponse calculateSemesterSummary(Long studentId, Long semesterId) {
        // 1. Get student and semester info
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Get all grades for the student in this semester
        List<Grades> semesterGrades = gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId);

        // 3. Group grades by subject
        Map<Subject, List<Grades>> gradesBySubject = semesterGrades.stream()
                .collect(Collectors.groupingBy(Grades::getSubject));

        // 4. Initialize counters
        int totalCredits = 0;
        int creditsEarned = 0;
        double totalGradePoints = 0;
        int passedSubjects = 0;
        List<ReportResponse.SubjectResult> subjectResults = new ArrayList<>();

        // 5. Process each subject
        for (Map.Entry<Subject, List<Grades>> entry : gradesBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Grades> subjectGrades = entry.getValue();

            // Calculate subject average (0-20 scale)
            double subjectAverage = GradeCalculator.calculateSubjectAverage(subjectGrades);
            boolean passed = subjectAverage >= 10.0;

            // Update totals
            int subjectCredits = subject.getCredits().intValue();
            totalCredits += subjectCredits;

            if (passed) {
                creditsEarned += subjectCredits;
                passedSubjects++;

                // Convert to GPA (0-4.0 scale) and weight by credits
                double subjectGPA = GradeCalculator.calculateGPA(subjectAverage);
                totalGradePoints += subjectGPA * subjectCredits;
            }

            // Add to subject results
            subjectResults.add(new ReportResponse.SubjectResult(
                    subject.getName(),
                    subjectAverage,
                    subject.getCredits(),
                    passed
            ));
        }

        // 6. Calculate semester GPA (weighted average of passed subjects)
        double semesterGPA = creditsEarned > 0 ?
                Math.round((totalGradePoints / creditsEarned) * 100.0) / 100.0 : 0.0;

        // 7. Create and return response
        ReportResponse response = new ReportResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        response.setSemesterId(semesterId);
        response.setSemesterName(semesterRepository.findById(semesterId)
                .map(sem -> sem.getName())
                .orElse("Semester " + semesterId));
        response.setGpa(semesterGPA);
        response.setCreditsEarned(creditsEarned);
        response.setStatus(calculateStatus(passedSubjects, gradesBySubject.size()));
        response.setSubjectResults(subjectResults);

        return response;
    }

    private String calculateStatus(int passedSubjects, int totalSubjects) {
        if (passedSubjects == 0) return "FAIL";
        if (passedSubjects == totalSubjects) return "PASS";
        return "INCOMPLETE";
    }

    public List<GradeResponse> getGradesByStudent(Long studentId) {
        // Implementation here
        return List.of();
    }

    public List<GradeResponse> getGradesBySubject(Long subjectId) {
        // Implementation here
        return List.of();
    }

    private GradeResponse convertToResponse(Grades grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setStudentName(grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());

        response.setSubjectId(grade.getSubject().getId());
        response.setSubjectName(grade.getSubject().getName());
        response.setSubjectCode(grade.getSubject().getCode());

        if (grade.getSemesters() != null) {
            response.setSemesterId(grade.getSemesters().getId());
            response.setSemesterName(grade.getSemesters().getName());
        }

        response.setValue(grade.getValue());
        response.setPeriodLabel(grade.getPeriodLabel());

        // Check if student passed the subject (score ≥ 10/20)
        boolean passed = grade.getValue() != null && grade.getValue() >= 10;
        response.setPassed(passed);

        // Award credits if passed
        if (passed && grade.getSubject().getCredits() != null) {
            response.setCreditsEarned(grade.getSubject().getCredits());
        } else {
            response.setCreditsEarned(java.math.BigDecimal.ZERO);
        }

        response.setType(grade.getType());
        response.setComments(grade.getComments());

        if (grade.getEnteredBy() != null) {
            response.setEnteredBy(grade.getEnteredBy().getId());
            response.setEnteredByName(grade.getEnteredBy().getFirstName() + " " + grade.getEnteredBy().getLastName());
        }

        response.setCreatedDate(grade.getCreatedDate());
        response.setLastModifiedDate(grade.getLastModifiedDate());

        return response;
    }

    public GradeResponse createGrade(GradeRequest gradeRequest) {
        // window validation for teachers
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isTeacher = auth!=null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        if (isTeacher) {
            if (!gradingWindowService.isWindowOpen(gradeRequest.getSemesterId(), gradeRequest.getPeriodLabel())) {
                String statusMessage = gradingWindowService.getWindowStatusMessage(gradeRequest.getSemesterId(), gradeRequest.getPeriodLabel());
                throw new RuntimeException(statusMessage);
            }
        }

        Grades grade = new Grades();
        grade.setStudent(studentRepository.findById(gradeRequest.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found")));
        grade.setSubject(subjectRepository.findById(gradeRequest.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found")));
        grade.setSemesters(semesterRepository.findById(gradeRequest.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        grade.setValue(gradeRequest.getValue());
        grade.setType(gradeRequest.getType());
        grade.setComments(gradeRequest.getComments());
        grade.setPeriodLabel(gradeRequest.getPeriodLabel());
        grade.setEnteredBy(userRepository.findById(gradeRequest.getEnteredBy())
                .orElseThrow(() -> new RuntimeException("User not found")));

        Grades saved = gradeRepository.save(grade);
        return convertToResponse(saved);
    }

    public GradeResponse createGradeByCode(com.university.ManageNotes.dto.Request.GradeByCodeRequest req) {
        // window validation for teacher (current auth is the caller)
        var auth2 = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isTeacher2 = auth2!=null && auth2.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_TEACHER"));
        if (isTeacher2) {
            if (!gradingWindowService.isWindowOpen(req.getSemesterId(), req.getPeriodLabel())) {
                String statusMessage = gradingWindowService.getWindowStatusMessage(req.getSemesterId(), req.getPeriodLabel());
                throw new RuntimeException(statusMessage);
            }
        }

        Students student = studentRepository.findByMatricule(req.getStudentMatricule())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        var subject = subjectRepository.findByCode(req.getSubjectCode())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Ensure current logged-in teacher is owner of the subject
        Long teacherId = null;
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof com.university.ManageNotes.security.UserPrincipal up) {
            teacherId = up.getId();
        }
        if (teacherId == null || !teacherId.equals(subject.getIdTeacher())) {
            throw new RuntimeException("You are not allowed to enter grades for this subject");
        }

        Grades grade = new Grades();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setSemesters(semesterRepository.findById(req.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        grade.setValue(req.getValue());
        grade.setType(req.getType());
        grade.setComments(req.getComments());
        grade.setPeriodLabel(req.getPeriodLabel());
        grade.setEnteredBy(userRepository.findById(teacherId).orElseThrow());

        Grades saved = gradeRepository.save(grade);
        return convertToResponse(saved);
    }

    public StudentGradesResponse getStudentGrades(Long studentId, Long semesterId) {
        List<Grades> grades;
        if (semesterId != null) {
            grades = gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId);
        } else {
            grades = gradeRepository.findByStudentId(studentId);
        }

        List<GradeResponse> gradeResponses = grades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        StudentGradesResponse response = new StudentGradesResponse();
        response.setStudentId(studentId);
        var studentOpt = studentRepository.findById(studentId);
        studentOpt.ifPresent(s -> response.setStudentName(s.getFirstName() + " " + s.getLastName()));
        if (semesterId != null) {
            var semOpt = semesterRepository.findById(semesterId);
            semOpt.ifPresent(se -> {
                response.setSemesterId(se.getId());
                response.setSemesterName(se.getName());
            });
        }
        response.setGrades(gradeResponses);

        // simple GPA calculation
        if (!grades.isEmpty()) {
            double avg = grades.stream().mapToDouble(Grades::getValue).average().orElse(0);
            response.setGpa(Math.round(avg * 100.0) / 100.0);
        }

        // Build TopicDto list expected by frontend
        java.util.Map<String, com.university.ManageNotes.dto.Response.TopicDto> topicMap = new java.util.HashMap<>();
        for (var gr : gradeResponses) {
            var topic = topicMap.computeIfAbsent(gr.getSubjectCode(), k -> {
                var subj = subjectRepository.findById(gr.getSubjectId()).orElse(null);
                return com.university.ManageNotes.dto.Response.TopicDto.builder()
                        .code(gr.getSubjectCode())
                        .title(gr.getSubjectName())
                        .credit(subj != null ? subj.getCredits() : java.math.BigDecimal.ZERO)
                        .semester(gr.getSemesterName() != null && gr.getSemesterName().toLowerCase().contains("2") ? "s2" : "s1")
                        .build();
            });
            if (gr.getPeriodLabel() != null) {
                if (gr.getPeriodLabel().toUpperCase().startsWith("CC")) {
                    topic.setCc(gr.getValue());
                } else if (gr.getPeriodLabel().toUpperCase().startsWith("SN")) {
                    topic.setSn(gr.getValue());
                }
            }
        }

        java.util.List<com.university.ManageNotes.dto.Response.TopicDto> topicList = new java.util.ArrayList<>(topicMap.values());

        response.setTopics(topicList);
        studentOpt.ifPresent(s -> {
            response.setFirstName(s.getFirstName());
            response.setLastName(s.getLastName());
            response.setEmail(s.getEmail());
            response.setUsername(s.getMatricule());
            if(s.getLevel()!=null) response.setLevel(s.getLevel().name());
            response.setRole("STUDENT");
        });

        return response;
    }

    public List<GradeResponse> getTeacherGrades() {
        Long teacherId = null;
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof com.university.ManageNotes.security.UserPrincipal up) {
                teacherId = up.getId();
            }
        } catch (Exception ignored) {}

        if (teacherId == null) {
            return java.util.List.of();
        }

        List<Grades> grades = gradeRepository.findByEnteredById(teacherId);
        return grades.stream().map(this::convertToResponse).collect(java.util.stream.Collectors.toList());
    }

    public com.university.ManageNotes.dto.Response.GradeSheetResponse getGradeSheet(String subjectCode, Long semesterId, String period) {
        var subject = subjectRepository.findByCode(subjectCode)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Ensure that, if a teacher is requesting, they own the subject
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof com.university.ManageNotes.security.UserPrincipal up) {
            boolean isAdmin = up.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin && !up.getId().equals(subject.getIdTeacher())) {
                throw new RuntimeException("You are not allowed to view the grade sheet for this subject");
            }
        }

        // Prepare response
        var resp = new com.university.ManageNotes.dto.Response.GradeSheetResponse();
        resp.setSubjectCode(subject.getCode());
        resp.setSubjectName(subject.getName());
        if (subject.getLevel() != null) resp.setLevel(subject.getLevel().name());
        if (subject.getCycle() != null) resp.setCycle(subject.getCycle().name());
        resp.setPeriod(period);

        java.util.List<com.university.ManageNotes.model.Students> students;
        if (semesterId != null) {
            students = getStudentsBySemester(semesterId);
        } else {
            // Fallback: all students who have grades for this subject
            var grades = gradeRepository.findBySubjectId(subject.getId());
            students = grades.stream().map(com.university.ManageNotes.model.Grades::getStudent).distinct().collect(java.util.stream.Collectors.toList());
        }

        resp.setTotalStudents(students.size());

        // Collect distinct (type,label) pairs
        java.util.Set<String> colKeys = new java.util.LinkedHashSet<>();
        java.util.Map<String, String> keyToLabel = new java.util.LinkedHashMap<>();

        java.util.List<com.university.ManageNotes.model.Grades> allGradesForSubject = gradeRepository.findBySubjectId(subject.getId());
        if (semesterId != null) {
            allGradesForSubject = allGradesForSubject.stream().filter(g -> g.getSemesters() != null && g.getSemesters().getId().equals(semesterId)).toList();
        }
        for (var g : allGradesForSubject) {
            String key = g.getType().name() + "::" + (g.getPeriodLabel() == null ? "" : g.getPeriodLabel());
            String label = g.getPeriodLabel() != null && !g.getPeriodLabel().isBlank() ? g.getPeriodLabel() : g.getType().name();
            colKeys.add(key);
            keyToLabel.put(key, label);
        }

        java.util.List<com.university.ManageNotes.dto.Response.GradeSheetColumn> cols = new java.util.ArrayList<>();
        for (String k : colKeys) {
            String[] parts = k.split("::",2);
            com.university.ManageNotes.model.GradeType t = com.university.ManageNotes.model.GradeType.valueOf(parts[0]);
            cols.add(new com.university.ManageNotes.dto.Response.GradeSheetColumn(t, keyToLabel.get(k)));
        }
        resp.setColumns(cols);
        // Build rows
        java.util.List<com.university.ManageNotes.dto.Response.GradeSheetRow> rows = new java.util.ArrayList<>();
        int conflicts = 0;
        for (var student : students) {
            var row = new com.university.ManageNotes.dto.Response.GradeSheetRow();
            row.setStudentId(student.getId());
            row.setMatricule(student.getMatricule());
            row.setFullName(student.getFirstName()+" "+student.getLastName());

            java.util.Map<String, Double> gradeMap = new java.util.HashMap<>();
            for (String k : colKeys) gradeMap.put(k, null);

            java.util.List<com.university.ManageNotes.model.Grades> sg;
            if (semesterId!=null) {
                sg = gradeRepository.findByStudentIdAndSemesterId(student.getId(), semesterId);
            } else sg = gradeRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId());

            for (var g: sg) {
                if (!g.getSubject().getId().equals(subject.getId())) continue;
                String k = g.getType().name()+"::"+(g.getPeriodLabel()==null?"":g.getPeriodLabel());
                if (gradeMap.get(k)!=null) conflicts++;// duplicate
                gradeMap.put(k, g.getValue());
            }

            long missing = gradeMap.values().stream().filter(java.util.Objects::isNull).count();
            conflicts += missing;

            double avg = gradeMap.values().stream().filter(java.util.Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0);
            row.setPassed(avg>=10);

            // convert map keys to label for dto
            java.util.Map<String, Double> labelMap = new java.util.HashMap<>();
            for (String k: gradeMap.keySet()) labelMap.put(keyToLabel.get(k), gradeMap.get(k));
            row.setGrades(labelMap);
            rows.add(row);
        }

        resp.setConflicts(conflicts);
        resp.setRows(rows);
        return resp;
    }

    public List<Students> getStudentsBySemester(Long semesterId) {
        List<Grades> grades = gradeRepository.findBySemesterId(semesterId);
        return grades.stream()
                .map(Grades::getStudent)
                .distinct()
                .collect(Collectors.toList());
    }
}
