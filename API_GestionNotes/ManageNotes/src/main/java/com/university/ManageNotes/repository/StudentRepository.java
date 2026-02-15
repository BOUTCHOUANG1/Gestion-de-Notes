package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);

    Optional<Student> findById(Long studentId);
    
    List<Student> findByStudentLevel(TeachingLevel teachingLevel);
    
    @Query("SELECT s FROM Student s WHERE s.studentLevel.studentLevel = :level")
    List<Student> findByStudentLevelEnum(@Param("level") StudentLevel level);
    
    @Query("SELECT s.studentLevel.studentLevel, COUNT(s) FROM Student s GROUP BY s.studentLevel.studentLevel")
    List<Object[]> countByLevel();
}
