package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);

    List<Subject> findByNameContainingIgnoreCase(String name);

    List<Subject> findByIdTeacher(Long teacherId);

    @Query("SELECT s FROM Subject s WHERE s.idTeacher = :teacherId ORDER BY s.name")
    List<Subject> findSubjectsByTeacherOrderByName(@Param("teacherId") Long teacherId);

    @Query("SELECT s FROM Subject s ORDER BY s.name")
    List<Subject> findAllOrderByName();
    
    List<Subject> findByDepartmentId(Long departmentId);
    
    @Query("SELECT s FROM Subject s WHERE s.idTeacher = :teacherId AND s.level = :level")
    Optional<Subject> findByTeacherIdAndLevel(@Param("teacherId") Long teacherId, @Param("level") com.university.ManageNotes.model.StudentLevel level);
    
    boolean existsByIdTeacherAndLevel(Long idTeacher, com.university.ManageNotes.model.StudentLevel level);
}
