package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.StudentLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
    Optional<Students> findByEmail(String email);

    @Query("""
            select distinct s
            from Students s
            join Subject subj
                on subj.level = s.level and subj.cycle = s.cycle
            where subj.idTeacher = :teacherId
            """)
    List<Students> findStudentsByTeacherSubject(Long teacherId);

    Optional<Students> findByMatricule(String matricule);

    List<Students> findByLevel(StudentLevel level);

}
