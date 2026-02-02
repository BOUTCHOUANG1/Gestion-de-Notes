package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RevendicationRepository extends JpaRepository<Revendication, Long> {
    
    boolean existsByStudentAndGradeAndStatus(Student student, Grades grade, RequestStatus status);
    
    Page<Revendication> findByGrade_Subject_TeacherAndStatusOrderByCreatedDateDesc(Teacher teacher, RequestStatus status, Pageable pageable);

    List<Revendication> findByStudentOrderByCreatedDateDesc(Student student);
    
    @Query("SELECT COUNT(r) FROM Revendication r WHERE r.status = :status")
    Long countByStatus(@Param("status") String status);
}
