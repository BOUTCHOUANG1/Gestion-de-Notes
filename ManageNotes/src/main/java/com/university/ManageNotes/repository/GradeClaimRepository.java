package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.GradeClaim;
import com.university.ManageNotes.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeClaimRepository extends JpaRepository<GradeClaim, Long> {
    List<GradeClaim> findByGrade_Subject_IdTeacher(Long teacherId);
}
