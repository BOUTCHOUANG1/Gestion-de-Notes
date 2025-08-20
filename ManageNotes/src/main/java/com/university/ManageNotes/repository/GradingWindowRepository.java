package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.GradingWindow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradingWindowRepository extends JpaRepository<GradingWindow, Long> {
    List<GradingWindow> findBySemesterId(Long idSemester);
    List<GradingWindow> findBySemesterIdAndNameIgnoreCase(Long idSemester, String name);
}
