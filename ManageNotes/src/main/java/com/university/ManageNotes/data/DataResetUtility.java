package com.university.ManageNotes.data;

import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(0) // Run before all other initializers
public class DataResetUtility implements CommandLineRunner {

    private final GradeRepository gradeRepository;
    private final TranscriptRepository transcriptRepository;
    private final RevendicationPeriodRepository revendicationPeriodRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only reset if specifically requested via environment variable
        String resetData = System.getProperty("reset.data", "false");
        
        if ("true".equalsIgnoreCase(resetData)) {
            System.out.println("🔄 Data reset requested - clearing existing data...");
            resetAllData();
            System.out.println("✅ Data reset completed");
        }
    }

    private void resetAllData() {
        // Delete in correct order to avoid foreign key constraints
        System.out.println("🗑️ Clearing grades...");
        gradeRepository.deleteAll();
        
        System.out.println("🗑️ Clearing transcripts...");
        transcriptRepository.deleteAll();
        
        System.out.println("🗑️ Clearing revendication periods...");
        revendicationPeriodRepository.deleteAll();
        
        System.out.println("🗑️ Clearing exams...");
        examRepository.deleteAll();
        
        // Clear subject-teacher relationships
        System.out.println("🗑️ Clearing subject-teacher assignments...");
        subjectRepository.findAll().forEach(subject -> {
            subject.setTeacher(null);
            subject.setSubjectLevel(null);
            subjectRepository.save(subject);
        });
        
        System.out.println("✅ All data cleared successfully");
    }
}