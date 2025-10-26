package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.model.TeachingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findSubjectByTeacher_Email(String teacherEmail);

    Optional<Subject> findBySubjectCode(String code);

    boolean existsByTeacherAndSubjectLevel(Teacher teacher, TeachingLevel subjectLevel);
    
    List<Subject> findByDepartment(Department department);
    
    @Modifying
    @Query("DELETE FROM Subject s WHERE s.subjectId = :id")
    void deleteSubjectById(@Param("id") Long id);
}
