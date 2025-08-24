package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Semesters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semesters, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("update Semesters s set s.active=false where s.id <> :id")
    void deactivateOtherSemesters(Long id);
}
