package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DefaultStudentInitializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public void run(String... args) throws Exception {
        // Example: create a sample student and generate grades for them
        Students student = new Students();
        student.setFirstName("Sample");
        student.setLastName("Student");
        student.setEmail("sample.student@university.com");
        studentRepository.save(student);

        // generate multiple grades for each subject
        var subjects = subjectRepository.findAll();
        var semesterOpt = semesterRepository.findAll().stream().findFirst();
        java.util.Random random = new java.util.Random();

        for (Subject subj : subjects) {
            // two CC grades
            for (int i = 1; i <= 2; i++) {
                Grades g = new Grades();
                g.setStudent(student);
                g.setSubject(subj);
                g.setValue(10 + random.nextDouble() * 10);
                g.setMaxValue(20.0);
                g.setComments("Sample auto grade");
                g.setPeriodLabel("CC #" + i);
                // skip grade type to avoid DB constraint
                semesterOpt.ifPresent(g::setSemester);
                gradeRepository.save(g);
            }

            // SN grade
            Grades sn = new Grades();
            sn.setStudent(student);
            sn.setSubject(subj);
            sn.setValue(10 + random.nextDouble() * 10);
            sn.setMaxValue(20.0);
            sn.setComments("Sample SN grade");
            sn.setPeriodLabel("SN #1");
            // skip type
            semesterOpt.ifPresent(sn::setSemester);
            gradeRepository.save(sn);

            // Exam grade using EXAM enum
            Grades ex = new Grades();
            ex.setStudent(student);
            ex.setSubject(subj);
            ex.setValue(10 + random.nextDouble() * 10);
            ex.setMaxValue(20.0);
            ex.setComments("Sample exam grade");
            ex.setPeriodLabel("EX #1");
            // skip type
            semesterOpt.ifPresent(ex::setSemester);
            gradeRepository.save(ex);
        }
    }
}
