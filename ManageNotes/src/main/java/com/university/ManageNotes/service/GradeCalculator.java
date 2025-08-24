package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.GradeType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeCalculator {
    // Default max values for different grade types
    private static final double DEFAULT_CC_MAX = 30.0;
    private static final double DEFAULT_SN_MAX = 70.0;
    private static final double DEFAULT_SN_WITH_PRACTICAL_MAX = 50.0;
    private static final double PRACTICAL_MAX = 20.0;
    private static final double TOTAL_MAX = 100.0;
    
    /**
     * Calculates the weighted average for a subject based on grade components
     * @param components List of grade components (CC, SN, Practical)
     * @return Weighted average out of 20
     */
    public static double calculateSubjectAverage(List<Grades> components) {
        if (components.isEmpty()) return 0.0;
        
        // Group grades by type
        Map<GradeType, List<Grades>> gradesByType = components.stream()
                .collect(Collectors.groupingBy(Grades::getType));
        
        boolean hasPractical = gradesByType.containsKey(GradeType.PRACTICAL);
        
        // Calculate weighted sum and total possible
        double weightedSum = 0.0;
        double totalPossible = 0.0;
        
        // Process CC grades (CC_1 and CC_2, each out of 30)
        if (gradesByType.containsKey(GradeType.CC_1) || gradesByType.containsKey(GradeType.CC_2)) {
            double ccSum = 0.0;
            if (gradesByType.containsKey(GradeType.CC_1)) {
                ccSum += gradesByType.get(GradeType.CC_1).stream()
                        .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                        .sum();
            }
            if (gradesByType.containsKey(GradeType.CC_2)) {
                ccSum += gradesByType.get(GradeType.CC_2).stream()
                        .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                        .sum();
            }
            weightedSum += ccSum;
            totalPossible += DEFAULT_CC_MAX;
        }
        
        // Process SN grades (SN_1 and SN_2, 70 or 50 depending on practical)
        if (gradesByType.containsKey(GradeType.SN_1) || gradesByType.containsKey(GradeType.SN_2)) {
            double snMax = hasPractical ? DEFAULT_SN_WITH_PRACTICAL_MAX : DEFAULT_SN_MAX;
            double snSum = 0.0;
            if (gradesByType.containsKey(GradeType.SN_1)) {
                snSum += gradesByType.get(GradeType.SN_1).stream()
                        .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                        .sum();
            }
            if (gradesByType.containsKey(GradeType.SN_2)) {
                snSum += gradesByType.get(GradeType.SN_2).stream()
                        .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                        .sum();
            }
            weightedSum += snSum;
            totalPossible += snMax;
        }
        
        // Process Practical grade (if exists)
        if (hasPractical) {
            double practicalSum = gradesByType.get(GradeType.PRACTICAL).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            weightedSum += practicalSum;
            totalPossible += PRACTICAL_MAX;
        }
        
        if (totalPossible == 0) return 0.0;
        
        // Calculate percentage and convert to 0-20 scale
        double percentage = (weightedSum / totalPossible) * 100.0;
        return Math.round((percentage / 5.0) * 100.0) / 100.0; // Convert to 0-20 scale
    }
    
    /**
     * Calculates GPA on a 4.0 scale based on the 0-20 average
     * @param average20 Average grade on 0-20 scale
     * @return GPA on 4.0 scale
     */
    public static double calculateGPA(double average20) {
        // Simple linear conversion from 0-20 to 0-4.0
        return Math.min(4.0, Math.round((average20 / 5.0) * 100.0) / 100.0);
    }
    
    /**
     * Determines if a student has validated all credits for a subject
     * @param studentGrades List of grades for a specific student and subject
     * @param allStudentsGrades List of grades for all students in the same subject
     * @return true if the student's average is above the class average
     */
    public static boolean hasValidatedCredits(List<Grades> studentGrades, List<Grades> allStudentsGrades) {
        if (studentGrades.isEmpty() || allStudentsGrades.isEmpty()) return false;
        
        // Get the student's average
        double studentAverage = calculateSubjectAverage(studentGrades);
        
        // Group grades by student and calculate each student's average
        Map<Long, List<Grades>> gradesByStudent = allStudentsGrades.stream()
                .collect(Collectors.groupingBy(g -> g.getStudent().getId()));
        
        // Calculate class average
        double classAverage = gradesByStudent.values().stream()
                .mapToDouble(GradeCalculator::calculateSubjectAverage)
                .average()
                .orElse(0.0);
                
        return studentAverage > classAverage;
    }
    
    // For backward compatibility
    @Deprecated
    public static double subjectAverage20(List<Grades> components) {
        return calculateSubjectAverage(components);
    }
    
    @Deprecated
    public static double subjectGpa4(List<Grades> components) {
        return calculateGPA(calculateSubjectAverage(components));
    }
}
