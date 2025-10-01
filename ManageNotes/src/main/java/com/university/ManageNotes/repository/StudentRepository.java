package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMatricule(String matricule);

    Optional<Student> findById(Long studentId);
    
    List<Student> findByStudentLevel(TeachingLevel teachingLevel);
}
