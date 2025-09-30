package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String departmentName);
    
    boolean existsByDepartmentNameAndDepartmentIdNot(String departmentName, Long departmentId);
}
