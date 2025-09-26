package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grades, Long> {
    @Query("SELECT g FROM Grades g " +
           "JOIN g.subject s " +
           "JOIN s.subjectsLevel tl " +
           "WHERE g.examiner.id = :teacherId " +
           "AND s.teacher.id = :teacherId " +
           "AND g.student.id = :studentId " +
           "AND tl = :teachingLevel " +
           "AND g.semester = :semester")
    List<Grades> findByTeacherAndStudentAndTeachingLevelAndSemester(
        @Param("teacherId") Long teacherId,
        @Param("studentId") Long studentId,
        @Param("teachingLevel") TeachingLevel teachingLevel,
        @Param("semester") Semester semester);

    List<Grades> findByStudentAndSemester(Student student, Semester semester);
    
    List<Grades> findByExaminer(Teacher teacher);
    
    List<Grades> findBySemester(Semester semester);
    
    boolean existsByStudentAndSubjectAndExamAndSemester(Student student, Subject subject, Exam exam, Semester semester);
}