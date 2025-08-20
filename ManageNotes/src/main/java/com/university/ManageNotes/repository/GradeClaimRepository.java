package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.GradeClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeClaimRepository extends JpaRepository<GradeClaim, Long> {
    List<GradeClaim> findBySemesterId(Long semesterId);
    List<GradeClaim> findByGradeSubjectId(Long subjectId);
    List<GradeClaim> findByStudentId(Long studentId);
    List<GradeClaim> findByGrade_Subject_IdIn(List<Long> subjectIds);
}
