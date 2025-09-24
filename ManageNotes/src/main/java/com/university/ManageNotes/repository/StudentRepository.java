package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.StudentLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    @Query("""
            select distinct s
            from Student s
            join Subject subj
                on subj.level = s.level and subj.cycle = s.cycle
            where subj.idTeacher = :teacherId
            """)
    List<Student> findStudentsByTeacherSubject(Long teacherId);

    Optional<Student> findByMatricule(String matricule);

    List<Student> findByLevel(StudentLevel level);

}
