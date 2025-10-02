package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String departmentName);
    
    boolean existsByDepartmentNameAndDepartmentIdNot(String departmentName, Long departmentId);
    
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.subjects WHERE d.departmentId = :id")
    Optional<Department> findByIdWithSubjects(@Param("id") Long id);
}
