package com.university.ManageNotes.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SequenceService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetUserSequence() {
        try {
            entityManager.createNativeQuery(
                "SELECT setval('users_seq', COALESCE((SELECT MAX(id) FROM users), 0) + 1, false)"
            ).getSingleResult();
        } catch (Exception e) {
            // Sequence reset failed, but don't break the application
            System.err.println("Failed to reset users sequence: " + e.getMessage());
        }
    }

    @Transactional
    public void resetStudentSequence() {
        try {
            entityManager.createNativeQuery(
                "SELECT setval('students_seq', COALESCE((SELECT MAX(id) FROM students), 0) + 1, false)"
            ).getSingleResult();
        } catch (Exception e) {
            System.err.println("Failed to reset students sequence: " + e.getMessage());
        }
    }
}