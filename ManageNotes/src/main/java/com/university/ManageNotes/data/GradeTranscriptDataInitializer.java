package com.university.ManageNotes.data;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AssessmentType;
import com.university.ManageNotes.model.enums.TranscriptStatus;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Random;

// @Component
@RequiredArgsConstructor
@Order(5)
public class GradeTranscriptDataInitializer implements CommandLineRunner {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;
    private final SemesterRepository semesterRepository;
    private final TeacherRepository teacherRepository;
    private final TranscriptRepository transcriptRepository;
    private final EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        long gradeCount = gradeRepository.count();
        long transcriptCount = transcriptRepository.count();
        
        System.out.println("📊 Current data: Grades=" + gradeCount + ", Transcripts=" + transcriptCount);
        
        // Check if we need to regenerate grades due to subject reassignments
        List<Subject> subjectsWithTeachers = subjectRepository.findAll().stream()
            .filter(s -> s.getTeacher() != null)
            .toList();
        
        if (gradeCount == 0 || subjectsWithTeachers.size() > 2) {
            if (gradeCount > 0 && subjectsWithTeachers.size() > 2) {
                System.out.println("🔄 Detected new teacher assignments, regenerating grades...");
                gradeRepository.deleteAll();
            }
            System.out.println("🚀 Starting grades initialization...");
            initializeGrades();
            System.out.println("✅ Grades initialization completed");
        } else {
            System.out.println("ℹ️ Grades already exist (" + gradeCount + " records)");
        }
        
        if (transcriptCount == 0) {
            System.out.println("🚀 Starting transcripts initialization...");
            initializeTranscripts();
            System.out.println("✅ Transcripts initialization completed");
        } else {
            System.out.println("ℹ️ Transcripts already exist (" + transcriptCount + " records)");
        }
    }

    @Transactional
    protected void initializeGrades() {
        List<Student> students = studentRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<Exam> exams = examRepository.findAll();
        List<Semester> semesters = semesterRepository.findAll();
        Random random = new Random();

        System.out.println("📊 Found " + students.size() + " students, " + subjects.size() + " subjects, " + exams.size() + " exams");

        for (Student student : students) {
            // Get subjects for student's level - skip if student has no level
            if (student.getStudentLevel() == null) {
                System.out.println("⚠️ Student " + student.getUsername() + " has no level assigned, skipping grades");
                continue;
            }
            
            // Get ALL subjects for student's level (not just one)
            List<Subject> levelSubjects = subjects.stream()
                .filter(s -> s.getSubjectLevel() != null && 
                           s.getSubjectLevel().getStudentLevel().equals(student.getStudentLevel().getStudentLevel()))
                .filter(s -> s.getTeacher() != null) // Only subjects with teachers
                .toList();
                
            if (levelSubjects.isEmpty()) {
                System.out.println("⚠️ No subjects with teachers found for student " + student.getUsername() + " at level " + student.getStudentLevel().getStudentLevel());
                continue;
            }

            System.out.println("📚 Creating grades for student " + student.getUsername() + " with " + levelSubjects.size() + " subjects");

            // Create grades for ALL subjects at student's level
            for (Subject subject : levelSubjects) {
                System.out.println("📝 Processing subject: " + subject.getSubjectName() + " taught by " + subject.getTeacher().getUsername());
                
                for (Semester semester : semesters) {
                    for (Exam exam : exams) {
                        // Check if grade already exists
                        if (!gradeRepository.existsByStudentAndSubjectAndExamAndSemester(student, subject, exam, semester)) {
                            createGrade(student, subject, exam, semester, subject.getTeacher(), random);
                        }
                    }
                }
            }
        }
    }

    private void createGrade(Student student, Subject subject, Exam exam, Semester semester, 
                           Teacher teacher, Random random) {
        Grades grade = new Grades();
        
        // Use EntityManager.getReference to get proxy entities
        Student studentRef = entityManager.getReference(Student.class, student.getId());
        Subject subjectRef = entityManager.getReference(Subject.class, subject.getSubjectId());
        Teacher teacherRef = entityManager.getReference(Teacher.class, teacher.getId());
        
        grade.setStudent(studentRef);
        grade.setSubject(subjectRef);
        grade.setExam(exam);
        grade.setSemester(semester);
        grade.setExaminer(teacherRef);
        
        // Generate realistic grades - 75% pass, 25% fail
        boolean shouldPass = random.nextDouble() < 0.75;
        double score = generateScore(exam.getAssessmentType(), shouldPass, random);
        
        // Set scores based on assessment type
        if (exam.getAssessmentType() == AssessmentType.CC_1 || exam.getAssessmentType() == AssessmentType.CC_2) {
            grade.setCcScore(score);
            grade.setSnScore(0.0); // Initialize SN score
        } else {
            grade.setSnScore(score);
            grade.setCcScore(0.0); // Initialize CC score
        }
        
        // Calculate total score (CC + SN for complete subject grade)
        double totalScore = grade.getCcScore() + grade.getSnScore();
        grade.setTotalScore(totalScore);
        
        // Calculate GPA and pass/fail status
        double gpa = convertToGPA((totalScore / 100.0) * 100);
        grade.setGpa(gpa);
        grade.setHasPassed(totalScore >= 50.0);
        
        grade.setComments(generateComment(score, exam.getAssessmentType()));
        grade.setCreatedDate(Instant.now());
        grade.setLastModifiedDate(Instant.now());
        
        gradeRepository.save(grade);
    }

    private double generateScore(AssessmentType assessmentType, boolean shouldPass, Random random) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> {
                // CC: 0-30 points, pass >= 15 (50% of 30)
                if (shouldPass) {
                    // Generate scores between 15-30 with distribution favoring higher scores
                    double baseScore = 15 + random.nextDouble() * 15;
                    // Add some excellent performers (20% chance for 25-30)
                    if (random.nextDouble() < 0.2) {
                        baseScore = 25 + random.nextDouble() * 5;
                    }
                    yield Math.min(30.0, baseScore);
                } else {
                    yield random.nextDouble() * 15; // 0-15
                }
            }
            case SN_1, SN_2 -> {
                // SN: 0-70 points, pass >= 35 (50% of 70)
                if (shouldPass) {
                    // Generate scores between 35-70 with distribution favoring higher scores
                    double baseScore = 35 + random.nextDouble() * 35;
                    // Add some excellent performers (20% chance for 60-70)
                    if (random.nextDouble() < 0.2) {
                        baseScore = 60 + random.nextDouble() * 10;
                    }
                    yield Math.min(70.0, baseScore);
                } else {
                    yield random.nextDouble() * 35; // 0-35
                }
            }
        };
    }

    private double getMaxValueForAssessment(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> 30.0;
            case SN_1, SN_2 -> 70.0;
        };
    }

    private String generateComment(double score, AssessmentType assessmentType) {
        double maxValue = getMaxValueForAssessment(assessmentType);
        double percentage = (score / maxValue) * 100;
        
        if (percentage >= 85) return "Excellent work!";
        if (percentage >= 70) return "Good performance";
        if (percentage >= 60) return "Satisfactory";
        if (percentage >= 50) return "Needs improvement";
        return "Requires significant improvement";
    }

    private void initializeTranscripts() {
        List<Student> students = studentRepository.findAll();
        
        System.out.println("📄 Initializing transcripts for " + students.size() + " students");

        int transcriptCount = 0;
        for (Student student : students) {
            if (createTranscript(student)) {
                transcriptCount++;
            }
        }
        
        System.out.println("✅ Created " + transcriptCount + " transcripts");
    }

    private boolean createTranscript(Student student) {
        // Check if student already has a transcript
        if (transcriptRepository.existsByStudent(student)) {
            return false;
        }
        
        // Get all grades for the student across all semesters
        List<Grades> allStudentGrades = gradeRepository.findByStudent(student);
        
        if (!allStudentGrades.isEmpty()) {
            Transcript transcript = new Transcript();
            transcript.setStudent(student);
            // Set to current active semester or first semester
            Semester activeSemester = semesterRepository.findByActiveTrue().orElse(
                semesterRepository.findAll().get(0)
            );
            transcript.setSemester(activeSemester);
            transcript.setCreatedDate(Instant.now());
            transcript.setLastModifiedDate(Instant.now());
            
            // Calculate GPA from all grades
            double gpa = calculateGPA(allStudentGrades);
            transcript.setGpa(gpa);
            
            // Set status based on performance
            transcript.setStatus(gpa >= 2.0 ? TranscriptStatus.PASSED : TranscriptStatus.FAILED);
            
            transcriptRepository.save(transcript);
            System.out.println("✅ Created transcript for " + student.getUsername() + " (GPA: " + String.format("%.2f", gpa) + ")");
            return true;
        } else {
            System.out.println("⚠️ No grades found for " + student.getUsername());
        }
        return false;
    }

    private double calculateGPA(List<Grades> grades) {
        double totalScore = 0;
        int subjectCount = 0;
        
        // Simple GPA calculation - convert to 4.0 scale
        for (Grades grade : grades) {
            double totalGrade = grade.getTotalScore() != null ? grade.getTotalScore() : 0.0;
            double percentage = (totalGrade / 100.0) * 100; // Total is already on 100 scale
            double gpaPoints = convertToGPA(percentage);
            totalScore += gpaPoints;
            subjectCount++;
        }
        
        return subjectCount > 0 ? totalScore / subjectCount : 0.0;
    }
    
    private double convertToGPA(double percentage) {
        if (percentage >= 90) return 4.0;  // A+
        if (percentage >= 85) return 3.7;  // A
        if (percentage >= 80) return 3.3;  // A-
        if (percentage >= 75) return 3.0;  // B+
        if (percentage >= 70) return 2.7;  // B
        if (percentage >= 65) return 2.3;  // B-
        if (percentage >= 60) return 2.0;  // C+
        if (percentage >= 55) return 1.7;  // C
        if (percentage >= 50) return 1.3;  // C-
        if (percentage >= 45) return 1.0;  // D
        return 0.0;  // F
    }
}