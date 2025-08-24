package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
