package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Transcript;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    Optional<Transcript> findByStudentAndSemester(Student student, Semester semester);
    boolean existsByStudentAndSemester(Student student, Semester semester);
}