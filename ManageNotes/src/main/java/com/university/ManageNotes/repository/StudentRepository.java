package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.StudentLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {

    List<Students> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<Students> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query("select distinct g.student from Grades g where g.subject.idTeacher = :teacherId")
    java.util.List<Students> findStudentsByTeacherId(Long teacherId);

    @org.springframework.data.jpa.repository.Query("""
            select distinct s
            from Students s
            join Subject subj
                on subj.level = s.level and subj.cycle = s.cycle
            where subj.idTeacher = :teacherId
            """)
    java.util.List<Students> findStudentsByTeacherSubject(Long teacherId);

    java.util.Optional<Students> findByMatricule(String matricule);

    java.util.List<Students> findByLevel(com.university.ManageNotes.model.StudentLevel level);

}
