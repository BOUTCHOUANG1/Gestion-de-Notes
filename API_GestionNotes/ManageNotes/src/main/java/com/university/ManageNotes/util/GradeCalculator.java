package com.university.ManageNotes.util;

import com.university.ManageNotes.model.enums.AssessmentType;

/**
 * Francophone university grading system (LMD).
 * All scores on /20. Weights: CC 30%, TP 20%, SN 50%.
 * Pass threshold: 10/20.
 */
public class GradeCalculator {

    private static final double CC_WEIGHT = 0.30;
    private static final double TP_WEIGHT = 0.20;
    private static final double SN_WEIGHT = 0.50;

    /**
     * Note UE = CC×0.30 + TP×0.20 + SN×0.50 (result on /20)
     */
    public static double calculateSubjectTotal(Double ccScore, Double tpScore, Double snScore) {
        double cc = (ccScore != null ? ccScore : 0) * CC_WEIGHT;
        double tp = (tpScore != null ? tpScore : 0) * TP_WEIGHT;
        double sn = (snScore != null ? snScore : 0) * SN_WEIGHT;
        return Math.round((cc + tp + sn) * 100.0) / 100.0;
    }

    /**
     * Francophone mention based on score /20
     */
    public static String getMention(double scoreOn20) {
        if (scoreOn20 >= 18) return "Excellent";
        if (scoreOn20 >= 16) return "Très Bien";
        if (scoreOn20 >= 14) return "Bien";
        if (scoreOn20 >= 12) return "Assez Bien";
        if (scoreOn20 >= 10) return "Passable";
        return "Échec";
    }

    /**
     * GPA on 4.0 scale from score /20
     */
    public static double calculateGPA(double scoreOn20) {
        if (scoreOn20 < 7)  return 0.0;
        if (scoreOn20 < 8)  return 1.0;
        if (scoreOn20 < 9)  return 1.3;
        if (scoreOn20 < 10) return 1.7;
        if (scoreOn20 < 11) return 2.0;
        if (scoreOn20 < 12) return 2.3;
        if (scoreOn20 < 13) return 2.7;
        if (scoreOn20 < 14) return 3.0;
        if (scoreOn20 < 15) return 3.3;
        if (scoreOn20 < 16) return 3.7;
        return 4.0;
    }

    /** Score is already on /20, no conversion needed */
    public static double convertTo20Scale(double scoreOn20) {
        return scoreOn20;
    }

    public static double calculateWeightedGPA(double gpa, int credits) {
        return gpa * credits;
    }

    public static double calculateWeightedScore(double scoreOn20, int credits) {
        return scoreOn20 * credits;
    }

    /** Pass if >= 10/20 */
    public static boolean hasPassed(double scoreOn20) {
        return scoreOn20 >= 10.0;
    }

    public static double getExamWeight(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> CC_WEIGHT;
            case SN_1, SN_2 -> SN_WEIGHT;
        };
    }
}