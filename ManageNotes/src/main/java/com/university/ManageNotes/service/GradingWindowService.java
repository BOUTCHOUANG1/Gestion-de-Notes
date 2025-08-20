package com.university.ManageNotes.service;

import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.repository.GradingWindowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GradingWindowService {
    private final GradingWindowRepository windowRepository;

    public boolean isWindowOpen(Long semesterId, String label) {
        if (label == null) return true; // if no period specified, let it pass
        var windows = windowRepository.findBySemesterIdAndNameIgnoreCase(semesterId, label);
        if (windows.isEmpty()) return false;
        LocalDate today = LocalDate.now();
        return windows.stream().anyMatch(w -> Boolean.TRUE.equals(w.getActive()) &&
                (w.getStartDate()==null || !today.isBefore(w.getStartDate())) &&
                (w.getEndDate()==null || !today.isAfter(w.getEndDate())));
    }
}
