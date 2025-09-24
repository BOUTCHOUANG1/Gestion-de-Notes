package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentName(@NotBlank(message = "Department name is required") @Size(min = 5, message = "Department name must be at least 5 characters long") String name);

    Department findDepartmentByTeacher_Id(Long teacherId);
}
