package com.university.ManageNotes.data;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.AssessmentType;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

// @Component
@RequiredArgsConstructor
@Order(4)
public class ExamRevendicationDataInitializer implements CommandLineRunner {

    private final ExamRepository examRepository;
    private final RevendicationPeriodRepository revendicationPeriodRepository;
    private final SemesterRepository semesterRepository;

    @Override
    public void run(String... args) throws Exception {
        long examCount = examRepository.count();
        long revendicationCount = revendicationPeriodRepository.count();
        
        System.out.println("📊 Current data: Exams=" + examCount + ", Revendication Periods=" + revendicationCount);
        
        if (examCount == 0) {
            System.out.println("🚀 Starting exam periods initialization...");
            initializeExamPeriods();
            System.out.println("✅ Exam periods initialized successfully");
        } else {
            System.out.println("ℹ️ Exam periods already exist (" + examCount + " records)");
        }
        
        if (revendicationCount == 0) {
            System.out.println("🚀 Starting revendication periods initialization...");
            initializeRevendicationPeriods();
            System.out.println("✅ Revendication periods initialized successfully");
        } else {
            System.out.println("ℹ️ Revendication periods already exist (" + revendicationCount + " records)");
        }
    }

    private void initializeExamPeriods() {
        System.out.println("📋 Creating exam periods for assessment types...");
        // Create exam periods for each assessment type
        for (AssessmentType assessmentType : AssessmentType.values()) {
            if (!examRepository.existsByAssessmentType(assessmentType)) {
                Exam exam = new Exam();
                exam.setAssessmentType(assessmentType);
                examRepository.save(exam);
                System.out.println("✅ Created exam period for: " + assessmentType);
            } else {
                System.out.println("ℹ️ Exam period already exists for: " + assessmentType);
            }
        }
    }

    private void initializeRevendicationPeriods() {
        List<Semester> semesters = semesterRepository.findAll();
        
        for (Semester semester : semesters) {
            if (semester.getName().toLowerCase().contains("1") || semester.getName().toLowerCase().contains("first")) {
                // Semester 1: CC_1 and SN_1 only
                createRevendicationPeriodForSemester(AssessmentType.CC_1, semester);
                createRevendicationPeriodForSemester(AssessmentType.SN_1, semester);
            } else {
                // Semester 2: CC_2 and SN_2 only
                createRevendicationPeriodForSemester(AssessmentType.CC_2, semester);
                createRevendicationPeriodForSemester(AssessmentType.SN_2, semester);
            }
        }
    }

    private void createRevendicationPeriodForSemester(AssessmentType assessmentType, Semester semester) {
        // Find the exam for this assessment type
        Exam exam = examRepository.findByAssessmentType(assessmentType)
                .orElseThrow(() -> new RuntimeException("Exam not found for assessment type: " + assessmentType));
        
        // Check if revendication period already exists
        if (revendicationPeriodRepository.existsByExamAndSemester(exam, semester)) {
            return;
        }
        
        RevendicationPeriod period = new RevendicationPeriod();
        period.setExam(exam);
        period.setSemester(semester);
        
        // Set revendication period dates based on assessment type and semester
        LocalDate[] dates = calculateRevendicationDates(assessmentType, semester);
        period.setStartDate(dates[0]);
        period.setEndDate(dates[1]);
        
        // Set color based on assessment type
        period.setColor(getColorForAssessmentType(assessmentType));
        period.setIsActive(true);
        period.setCreatedDate(Instant.now());
        period.setLastModifiedDate(Instant.now());
        
        revendicationPeriodRepository.save(period);
        System.out.println("✅ Created revendication period for " + assessmentType + " in " + semester.getName());
    }

    private LocalDate[] calculateRevendicationDates(AssessmentType assessmentType, Semester semester) {
        LocalDate semesterStart = semester.getStartDate();
        LocalDate semesterEnd = semester.getEndDate();
        
        return switch (assessmentType) {
            case CC_1 -> {
                // CC1 revendication: 2 weeks after semester start, lasts 1 week
                LocalDate start = semesterStart.plusWeeks(2);
                yield new LocalDate[]{start, start.plusWeeks(1)};
            }
            case CC_2 -> {
                // CC2 revendication: mid-semester, lasts 1 week
                LocalDate start = semesterStart.plusDays(semesterStart.until(semesterEnd).getDays() / 2);
                yield new LocalDate[]{start, start.plusWeeks(1)};
            }
            case SN_1 -> {
                // SN1 revendication: 3/4 through semester, lasts 1 week
                LocalDate start = semesterStart.plusDays((semesterStart.until(semesterEnd).getDays() * 3) / 4);
                yield new LocalDate[]{start, start.plusWeeks(1)};
            }
            case SN_2 -> {
                // SN2 revendication: 1 week after semester end, lasts 2 weeks
                LocalDate start = semesterEnd.plusWeeks(1);
                yield new LocalDate[]{start, start.plusWeeks(2)};
            }
        };
    }

    private String getColorForAssessmentType(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1 -> "#4CAF50"; // Green
            case CC_2 -> "#2196F3"; // Blue
            case SN_1 -> "#FF9800"; // Orange
            case SN_2 -> "#F44336"; // Red
        };
    }
}