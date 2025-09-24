package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Revendication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevendicationRepository extends JpaRepository<Revendication, Long> {
    List<Revendication> findByGrade_Subject_IdTeacher(Long teacherId);
}
