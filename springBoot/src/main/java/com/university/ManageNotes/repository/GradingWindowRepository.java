package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.model.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradingWindowRepository extends JpaRepository<GradingWindow, Long> {
    List<GradingWindow> findBySemesterId(Long idSemester);
    List<GradingWindow> findBySemesterIdAndShortNameIgnoreCase(Long idSemester, String shortName);
    List<GradingWindow> findBySemesterIdAndPeriodLabel(Long idSemester, PeriodType periodLabel);
    List<GradingWindow> findBySemesterIdAndIsActive(Long idSemester, Boolean isActive);
    List<GradingWindow> findAllByOrderByOrderAsc();
    List<GradingWindow> findByIsActiveTrue();
}
