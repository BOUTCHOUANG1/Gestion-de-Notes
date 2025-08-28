package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeRequest;
import com.university.ManageNotes.dto.Request.GradeUpdateRequest;
import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.model.GradeType;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final SemesterRepository semesterRepository;
    private final GradingWindowService gradingWindowService;

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
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Grades> semesterGrades = gradeRepository.findByStudentIdAndSemesterId(studentId, semesterId);
        Map<Subject, List<Grades>> gradesBySubject = semesterGrades.stream()
                .collect(Collectors.groupingBy(Grades::getSubject));

        List<ReportResponse.SubjectResult> subjectResults = new ArrayList<>();
        
        for (Map.Entry<Subject, List<Grades>> entry : gradesBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Grades> subjectGrades = entry.getValue();
            double subjectAverage = GradeCalculator.calculateSubjectAverage(subjectGrades);
            boolean passed = subjectAverage >= 10.0;

            subjectResults.add(new ReportResponse.SubjectResult(
                    subject.getName(),
                    subjectAverage,
                    subject.getCredits(),
                    passed
            ));
        }

        // University calculations
        double semesterAverage = GradeCalculator.calculateSemesterAverage(semesterGrades);
        int validatedCredits = GradeCalculator.calculateValidatedCredits(semesterGrades);
        double semesterGPA = GradeCalculator.calculateGPA(semesterAverage);

        ReportResponse response = new ReportResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        response.setSemesterId(semesterId);
        response.setSemesterName(semesterRepository.findById(semesterId)
                .map(sem -> sem.getName())
                .orElse("Semester " + semesterId));
        response.setGpa(semesterGPA);
        response.setCreditsEarned(validatedCredits);
        response.setStatus(validatedCredits >= 30 ? "PASS" : "FAIL");
        response.setSubjectResults(subjectResults);

        return response;
    }
    
    public ReportResponse calculateYearSummary(Long studentId, Long semester1Id, Long semester2Id) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get grades for both semesters
        List<Grades> semester1Grades = gradeRepository.findByStudentIdAndSemesterId(studentId, semester1Id);
        List<Grades> semester2Grades = gradeRepository.findByStudentIdAndSemesterId(studentId, semester2Id);

        // Calculate semester averages and credits
        double semester1Average = GradeCalculator.calculateSemesterAverage(semester1Grades);
        double semester2Average = GradeCalculator.calculateSemesterAverage(semester2Grades);
        int semester1Credits = GradeCalculator.calculateValidatedCredits(semester1Grades);
        int semester2Credits = GradeCalculator.calculateValidatedCredits(semester2Grades);
        int totalValidatedCredits = semester1Credits + semester2Credits;
        
        // Calculate year GPA and promotion status
        double yearGPA = GradeCalculator.calculateYearGPA(semester1Average, semester2Average);
        boolean canBePromoted = GradeCalculator.canBePromoted(totalValidatedCredits, yearGPA);
        
        // Determine promotion reason
        String promotionReason = "";
        if (!canBePromoted) {
            if (totalValidatedCredits < 55) {
                promotionReason = "Insufficient credits: " + totalValidatedCredits + "/60 (minimum 55 required)";
            } else if (yearGPA <= 2.0) {
                promotionReason = "GPA too low: " + yearGPA + "/4.0 (minimum 2.0 required)";
            }
        } else {
            promotionReason = "Meets all requirements: " + totalValidatedCredits + " credits, GPA " + yearGPA;
        }

        ReportResponse response = new ReportResponse();
        response.setStudentId(studentId);
        response.setStudentName(student.getFirstName() + " " + student.getLastName());
        response.setGpa(yearGPA);
        response.setCreditsEarned(totalValidatedCredits);
        response.setStatus(canBePromoted ? "PROMOTED" : "NOT_PROMOTED");
        
        // University promotion fields
        response.setTotalCreditsRequired(60);
        response.setSemester1Credits(semester1Credits);
        response.setSemester2Credits(semester2Credits);
        response.setSemester1Average(semester1Average);
        response.setSemester2Average(semester2Average);
        response.setPromotionEligible(canBePromoted);
        response.setPromotionReason(promotionReason);
        
        return response;
    }




    private GradeResponse convertToResponse(Grades grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        // For API consistency, return user ID but get name from student record
        var user = userRepository.findByUsername(grade.getStudent().getMatricule())
                .orElse(null);
        response.setStudentId(user != null ? user.getId() : grade.getStudent().getId());
        response.setStudentName(grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());

        response.setSubjectId(grade.getSubject().getId());
        response.setSubjectName(grade.getSubject().getName());
        response.setSubjectCode(grade.getSubject().getCode());

        if (grade.getSemesters() != null) {
            response.setSemesterId(grade.getSemesters().getId());
            response.setSemesterName(grade.getSemesters().getName());
        }

        response.setValue(grade.getValue());
        response.setPeriodLabel(grade.getPeriodType() != null ? grade.getPeriodType().name() : null);

        // Check if student passed the subject (score ≥ 10/20)
        boolean passed = grade.getValue() != null && grade.getValue() >= 10;
        response.setPassed(passed);

        // Award credits if passed
        if (passed && grade.getSubject().getCredits() != null) {
            response.setCreditsEarned(grade.getSubject().getCredits());
        } else {
            response.setCreditsEarned(BigDecimal.ZERO);
        }

        response.setType(grade.getType());
        response.setComments(grade.getComments());

        if (grade.getEnteredBy() != null) {
            response.setEnteredBy(grade.getEnteredBy().getId());
            response.setEnteredByName(grade.getEnteredBy()
                    .getFirstName() + " " + grade.getEnteredBy().getLastName());
        }

        response.setCreatedDate(grade.getCreatedDate());
        response.setLastModifiedDate(grade.getLastModifiedDate());

        return response;
    }

    public GradeResponse createGrade(GradeRequest gradeRequest) {
        // window validation for teachers
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isTeacher = auth!=null && auth.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));
        if (isTeacher) {
            if (!gradingWindowService.isWindowOpen(gradeRequest.getSemesterId(), gradeRequest.getPeriodType())) {
                String statusMessage = gradingWindowService.getWindowStatusMessage(gradeRequest.getSemesterId(), gradeRequest.getPeriodType());
                throw new RuntimeException(statusMessage);
            }
        }

        // Convert user ID to student record for consistency
        Students student = getStudentByUserId(gradeRequest.getStudentId());

        Grades grade = new Grades();
        grade.setStudent(student);
        grade.setSubject(subjectRepository.findById(gradeRequest.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found")));
        grade.setSemesters(semesterRepository.findById(gradeRequest.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));
        grade.setValue(gradeRequest.getValue());
        grade.setMaxValue(gradeRequest.getMaxValue());
        grade.setType(gradeRequest.getType());
        grade.setComments(gradeRequest.getComments());
        grade.setPeriodType(gradeRequest.getPeriodType());
        grade.setEnteredBy(userRepository.findById(gradeRequest.getEnteredBy())
                .orElseThrow(() -> new RuntimeException("User not found")));

        Grades saved = gradeRepository.save(grade);
        return convertToResponse(saved);
    }

    public StudentGradesResponse getStudentGrades(Long userId, Long semesterId) {
        // Convert user ID to student ID for grade lookup
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!"STUDENT".equals(user.getRole().name())) {
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
                    .mapToDouble(Grades::getValue)
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

    public com.university.ManageNotes.dto.Response.GradeSheetResponse getGradeSheet(String subjectCode, Long semesterId, String period) {
        var subject = subjectRepository.findByCode(subjectCode)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Ensure that, if a teacher is requesting, they own the subject
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            boolean isAdmin = up.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin && !up.getId().equals(subject.getIdTeacher())) {
                throw new RuntimeException("You are not allowed to view the grade sheet for this subject");
            }
        }

        // Prepare response
        var resp = new GradeSheetResponse();
        resp.setSubjectCode(subject.getCode());
        resp.setSubjectName(subject.getName());
        if (subject.getLevel() != null) resp
                .setLevel(subject.getLevel().name());
        if (subject.getCycle() != null) resp
                .setCycle(subject.getCycle().name());
        resp.setPeriod(period);

        List<Students> students;
        if (semesterId != null) {
            students = getStudentsBySemester(semesterId);
        } else {
            // Fallback: all students who have grades for this subject
            var grades = gradeRepository.findBySubjectId(subject.getId());
            students = grades.stream()
                    .map(Grades::getStudent)
                    .distinct()
                    .collect(Collectors.toList());
        }

        resp.setTotalStudents(students.size());

        // Collect distinct (type,label) pairs
        Set<String> colKeys = new LinkedHashSet<>();
        Map<String, String> keyToLabel = new LinkedHashMap<>();

        List<Grades> allGradesForSubject = gradeRepository.findBySubjectId(subject.getId());
        if (semesterId != null) {
            allGradesForSubject = allGradesForSubject.stream()
                    .filter(g -> g.getSemesters() != null && g.getSemesters().getId()
                            .equals(semesterId))
                    .toList();
        }
        for (var g : allGradesForSubject) {
            String key = g.getType().name() + "::" + (g.getPeriodType() == null ? "" : g.getPeriodType().name());
            String label = g.getPeriodType() != null ? g.getPeriodType().name() : g.getType().name();
            colKeys.add(key);
            keyToLabel.put(key, label);
        }

        List<GradeSheetColumn> cols = new ArrayList<>();
        for (String k : colKeys) {
            String[] parts = k.split("::",2);
            GradeType t = GradeType.valueOf(parts[0]);
            cols.add(new GradeSheetColumn(t, keyToLabel.get(k)));
        }
        resp.setColumns(cols);
        // Build rows
        List<GradeSheetRow> rows = new ArrayList<>();
        int conflicts = 0;
        for (var student : students) {
            var row = new GradeSheetRow();
            row.setStudentId(student.getId());
            row.setMatricule(student.getMatricule());
            row.setFullName(student.getFirstName()+" "+student.getLastName());

            java.util.Map<String, Double> gradeMap = new HashMap<>();
            for (String k : colKeys) gradeMap.put(k, null);

            List<Grades> sg;
            if (semesterId!=null) {
                sg = gradeRepository.findByStudentIdAndSemesterId(student.getId(), semesterId);
            } else sg = gradeRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId());

            for (var g: sg) {
                if (!g.getSubject().getId().equals(subject.getId())) continue;
                String k = g.getType().name()+"::"+(g.getPeriodType()==null?"":g.getPeriodType().name());
                if (gradeMap.get(k)!=null) conflicts++;// duplicate
                gradeMap.put(k, g.getValue());
            }

            long missing = gradeMap.values().stream()
                    .filter(Objects::isNull).count();
            conflicts += missing;

            double avg = gradeMap.values().stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(Double::doubleValue)
                    .average().orElse(0);
            row.setPassed(avg>=10);

            // convert map keys to label for dto
            Map<String, Double> labelMap = new HashMap<>();
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
    
    /**
     * Ensures data consistency between users and students tables for grade operations
     */
    private Students getStudentByUserId(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        if (!"STUDENT".equals(user.getRole().name())) {
            throw new RuntimeException("User with ID " + userId + " is not a student");
        }
        
        return studentRepository.findByMatricule(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Student record not found for user: " + user.getUsername()));
    }
}
