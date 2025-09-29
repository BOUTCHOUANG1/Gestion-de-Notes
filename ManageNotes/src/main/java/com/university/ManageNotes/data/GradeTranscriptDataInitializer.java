package com.university.ManageNotes.data;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AssessmentType;
import com.university.ManageNotes.model.enums.TranscriptStatus;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Component
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

    @Override
    public void run(String... args) throws Exception {
        long gradeCount = gradeRepository.count();
        long transcriptCount = transcriptRepository.count();
        
        System.out.println("📊 Current data: Grades=" + gradeCount + ", Transcripts=" + transcriptCount);
        
        // Always try to initialize if data is missing
        if (gradeCount == 0) {
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

    private void initializeGrades() {
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
            
            List<Subject> levelSubjects = subjects.stream()
                .filter(s -> s.getSubjectLevel() != null && 
                           s.getSubjectLevel().getStudentLevel().equals(student.getStudentLevel().getStudentLevel()))
                .toList();
                
            if (levelSubjects.isEmpty()) {
                System.out.println("⚠️ No subjects found for student " + student.getUsername() + " at level " + student.getStudentLevel().getStudentLevel());
                continue;
            }

            System.out.println("📚 Creating grades for student " + student.getUsername() + " with " + levelSubjects.size() + " subjects");

            for (Subject subject : levelSubjects) {
                if (subject.getTeacher() != null) {
                    for (Semester semester : semesters) {
                        for (Exam exam : exams) {
                            // Check if grade already exists
                            if (!gradeRepository.existsByStudentAndSubjectAndExamAndSemester(student, subject, exam, semester)) {
                                createGrade(student, subject, exam, semester, subject.getTeacher(), random);
                            }
                        }
                    }
                } else {
                    System.out.println("⚠️ Subject " + subject.getSubjectName() + " has no teacher assigned");
                }
            }
        }
    }

    private void createGrade(Student student, Subject subject, Exam exam, Semester semester, 
                           Teacher teacher, Random random) {
        Grades grade = new Grades();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setExam(exam);
        grade.setSemester(semester);
        grade.setExaminer(teacher);
        
        // Generate realistic grades - 70% pass, 30% fail
        boolean shouldPass = random.nextDouble() < 0.7;
        double score = generateScore(exam.getAssessmentType(), shouldPass, random);
        
        // Set scores based on assessment type
        if (exam.getAssessmentType() == AssessmentType.CC_1 || exam.getAssessmentType() == AssessmentType.CC_2) {
            grade.setCcScore(score);
            grade.setSnScore(0.0); // Initialize SN score
        } else {
            grade.setSnScore(score);
            grade.setCcScore(0.0); // Initialize CC score
        }
        
        // Calculate total score
        grade.setTotalScore(grade.getCcScore() + grade.getSnScore());
        grade.setComments(generateComment(score, exam.getAssessmentType()));
        grade.setCreatedDate(Instant.now());
        grade.setLastModifiedDate(Instant.now());
        
        gradeRepository.save(grade);
    }

    private double generateScore(AssessmentType assessmentType, boolean shouldPass, Random random) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> {
                // CC: 0-30 points, pass >= 18
                if (shouldPass) {
                    yield 18 + random.nextDouble() * 12; // 18-30
                } else {
                    yield random.nextDouble() * 18; // 0-18
                }
            }
            case SN_1, SN_2 -> {
                // SN: 0-70 points, pass >= 42
                if (shouldPass) {
                    yield 42 + random.nextDouble() * 28; // 42-70
                } else {
                    yield random.nextDouble() * 42; // 0-42
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
        List<Semester> semesters = semesterRepository.findAll();
        
        System.out.println("📄 Initializing transcripts for " + students.size() + " students and " + semesters.size() + " semesters");

        int transcriptCount = 0;
        for (Student student : students) {
            for (Semester semester : semesters) {
                if (createTranscript(student, semester)) {
                    transcriptCount++;
                }
            }
        }
        
        System.out.println("✅ Created " + transcriptCount + " transcripts");
    }

    private boolean createTranscript(Student student, Semester semester) {
        // Check if student has grades for this semester
        List<Grades> studentGrades = gradeRepository.findByStudentAndSemester(student, semester);
        
        if (!studentGrades.isEmpty() && !transcriptRepository.existsByStudentAndSemester(student, semester)) {
            Transcript transcript = new Transcript();
            transcript.setStudent(student);
            transcript.setSemester(semester);
            transcript.setCreatedDate(Instant.now());
            transcript.setLastModifiedDate(Instant.now());
            
            // Calculate GPA
            double gpa = calculateGPA(studentGrades);
            transcript.setGpa(gpa);
            
            // Set status based on performance
            transcript.setStatus(gpa >= 2.0 ? TranscriptStatus.PASSED : TranscriptStatus.FAILED);
            
            transcriptRepository.save(transcript);
            System.out.println("✅ Created transcript for " + student.getUsername() + " - " + semester.getName() + " (GPA: " + String.format("%.2f", gpa) + ")");
            return true;
        } else if (studentGrades.isEmpty()) {
            System.out.println("⚠️ No grades found for " + student.getUsername() + " in " + semester.getName());
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
        if (percentage >= 90) return 4.0;
        if (percentage >= 80) return 3.0;
        if (percentage >= 70) return 2.0;
        if (percentage >= 60) return 1.0;
        return 0.0;
    }
}