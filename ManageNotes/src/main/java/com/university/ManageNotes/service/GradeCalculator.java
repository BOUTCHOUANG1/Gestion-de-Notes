package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.GradeType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeCalculator {
    // University grade system constants
    private static final double DEFAULT_CC_MAX = 30.0;  // CC out of 30
    private static final double DEFAULT_SN_MAX = 70.0;  // SN out of 70 (without practical)
    private static final double DEFAULT_SN_WITH_PRACTICAL_MAX = 50.0; // SN out of 50 (with practical)
    private static final double PRACTICAL_MAX = 20.0;   // Practical out of 20
    
    /**
     * Calculates the weighted average for a subject based on grade components
     * University system: Each semester (CC+SN) is calculated separately, then averaged
     * @param components List of grade components (CC_1, SN_1, CC_2, SN_2, Practical)
     * @return Weighted average out of 20
     */
    public static double calculateSubjectAverage(List<Grades> components) {
        if (components.isEmpty()) return 0.0;
        
        Map<GradeType, List<Grades>> gradesByType = components.stream()
                .collect(Collectors.groupingBy(Grades::getType));
        
        boolean hasPractical = gradesByType.containsKey(GradeType.PRACTICAL);
        double snMax = hasPractical ? DEFAULT_SN_WITH_PRACTICAL_MAX : DEFAULT_SN_MAX;
        
        double semester1Average = 0.0;
        double semester2Average = 0.0;
        int semesterCount = 0;
        
        // Calculate Semester 1 (CC_1 + SN_1)
        if (gradesByType.containsKey(GradeType.CC_1) && gradesByType.containsKey(GradeType.SN_1)) {
            double cc1 = gradesByType.get(GradeType.CC_1).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            double sn1 = gradesByType.get(GradeType.SN_1).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            semester1Average = ((cc1 + sn1) / (DEFAULT_CC_MAX + snMax)) * 20.0;
            semesterCount++;
        }
        
        // Calculate Semester 2 (CC_2 + SN_2)
        if (gradesByType.containsKey(GradeType.CC_2) && gradesByType.containsKey(GradeType.SN_2)) {
            double cc2 = gradesByType.get(GradeType.CC_2).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            double sn2 = gradesByType.get(GradeType.SN_2).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            semester2Average = ((cc2 + sn2) / (DEFAULT_CC_MAX + snMax)) * 20.0;
            semesterCount++;
        }
        
        if (semesterCount == 0) return 0.0;
        
        // Average the semesters
        double yearAverage = (semester1Average + semester2Average) / semesterCount;
        
        // Add practical component if exists (bonus points)
        if (hasPractical) {
            double practicalSum = gradesByType.get(GradeType.PRACTICAL).stream()
                    .mapToDouble(g -> g.getValue() != null ? g.getValue() : 0.0)
                    .sum();
            yearAverage += (practicalSum / PRACTICAL_MAX) * 2.0; // Practical adds up to 2 points
        }
        
        return Math.min(20.0, Math.round(yearAverage * 100.0) / 100.0);
    }
    
    /**
     * Calculates GPA on a 4.0 scale based on the 0-20 average
     * @param average20 Average grade on 0-20 scale
     * @return GPA on 4.0 scale
     */
    public static double calculateGPA(double average20) {
        return Math.min(4.0, Math.round((average20 / 5.0) * 100.0) / 100.0);
    }
    
    /**
     * Calculates semester average from CC and SN grades for all subjects in that semester
     * @param semesterGrades All grades for a student in a specific semester
     * @return Semester average out of 20
     */
    public static double calculateSemesterAverage(List<Grades> semesterGrades) {
        Map<Long, List<Grades>> gradesBySubject = semesterGrades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getId()));
        
        double totalWeightedScore = 0.0;
        double totalCredits = 0.0;
        
        for (List<Grades> subjectGrades : gradesBySubject.values()) {
            double subjectAverage = calculateSubjectAverage(subjectGrades);
            double subjectCredits = subjectGrades.get(0).getSubject().getCredits().doubleValue();
            
            totalWeightedScore += subjectAverage * subjectCredits;
            totalCredits += subjectCredits;
        }
        
        return totalCredits > 0 ? totalWeightedScore / totalCredits : 0.0;
    }
    
    /**
     * Calculates validated credits for a semester (credits for subjects with average >= 10)
     * @param semesterGrades All grades for a student in a specific semester
     * @return Number of validated credits
     */
    public static int calculateValidatedCredits(List<Grades> semesterGrades) {
        Map<Long, List<Grades>> gradesBySubject = semesterGrades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getId()));
        
        return gradesBySubject.entrySet().stream()
                .mapToInt(entry -> {
                    double subjectAverage = calculateSubjectAverage(entry.getValue());
                    return subjectAverage >= 10.0 ? 
                           entry.getValue().get(0).getSubject().getCredits().intValue() : 0;
                })
                .sum();
    }
    
    /**
     * Determines if student can be promoted to next level
     * @param totalValidatedCredits Total credits validated across both semesters
     * @param yearGPA GPA for the academic year
     * @return true if student meets promotion criteria (>= 55 credits and GPA > 2.0)
     */
    public static boolean canBePromoted(int totalValidatedCredits, double yearGPA) {
        return totalValidatedCredits >= 55 && yearGPA > 2.0;
    }
    
    /**
     * Calculates year GPA from both semester averages
     * @param semester1Average Average for semester 1 (out of 20)
     * @param semester2Average Average for semester 2 (out of 20)
     * @return GPA on 4.0 scale
     */
    public static double calculateYearGPA(double semester1Average, double semester2Average) {
        double yearAverage = (semester1Average + semester2Average) / 2.0;
        return calculateGPA(yearAverage);
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
