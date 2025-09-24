package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("update Semester s set s.active=false where s.id <> :id")
    void deactivateOtherSemesters(Long id);
}
