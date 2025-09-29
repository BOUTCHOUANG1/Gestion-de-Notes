package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachingLevelRepository extends JpaRepository<TeachingLevel, Long> {
    Optional<TeachingLevel> findByStudentLevel(StudentLevel studentLevel);
    boolean existsByStudentLevel(StudentLevel studentLevel);
}