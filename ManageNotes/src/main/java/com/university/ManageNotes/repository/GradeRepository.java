package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grades, Long> {

    /**
     * Find grades by student ID.
     * Note: studentId refers to students.id (not users.id)
     */
    List<Grades> findByStudentId(Long studentId);

    List<Grades> findBySubjectId(Long subjectId);

    @Query("SELECT g FROM Grades g WHERE g.semester.id = :semesterId")
    List<Grades> findBySemesterId(@Param("semesterId") Long semesterId);

    List<Grades> findByEnteredById(Long teacherId);

    @Query("SELECT g FROM Grades g WHERE g.student.id = :studentId AND g.semester.id = :semesterId")
    List<Grades> findByStudentIdAndSemesterId(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);

    List<Grades> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    void deleteByEnteredBy(Users enteredBy);
}