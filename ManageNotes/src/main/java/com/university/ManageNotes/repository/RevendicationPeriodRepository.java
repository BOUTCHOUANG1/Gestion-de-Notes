package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.ExamPeriod;
import com.university.ManageNotes.model.RevendicationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevendicationPeriodRepository extends JpaRepository<RevendicationPeriod, Long> {
    List<RevendicationPeriod> findBySemesterId(Long idSemester);
    List<RevendicationPeriod> findBySemesterIdAndShortNameIgnoreCase(Long idSemester, String shortName);
    List<RevendicationPeriod> findBySemesterIdAndExamPeriod(Long idSemester, ExamPeriod periodLabel);
    List<RevendicationPeriod> findBySemesterIdAndIsActive(Long idSemester, Boolean isActive);
    List<RevendicationPeriod> findAllByOrderByOrderAsc();
    List<RevendicationPeriod> findByIsActiveTrue();
}
