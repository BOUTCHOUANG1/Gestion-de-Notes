package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findById(Long id);
    Optional<Teacher> findByUsername(String username);
    
    @Query("SELECT DISTINCT t FROM Teacher t " +
           "LEFT JOIN FETCH t.department d " +
           "LEFT JOIN FETCH d.subjects " +
           "LEFT JOIN FETCH t.teachingLevels")
    List<Teacher> findAllWithDepartmentSubjects();
}
