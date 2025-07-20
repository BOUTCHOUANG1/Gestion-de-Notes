package com.university.ManageNotes.service;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultStudentInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // create default student user 'student'
        if (userRepository.findByUsername("student").isEmpty()) {
            Users user = new Users();
            user.setUsername("student");
            user.setPassword(passwordEncoder.encode("student"));
            user.setRole(Role.STUDENT);
            user.setEmail("student@example.com");
            user.setFirstName("Default");
            user.setLastName("Student");
            user.setActive(true);
            user = userRepository.save(user);

            // link to Students entity
            Students student = new Students();
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setEmail(user.getEmail());
            student.setMatricule("MAT" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            student.setLevel(StudentLevel.LEVEL1);
            student.setCycle(StudentCycle.BACHELOR);
            student.setSpeciality("Computer Science");
            student.setDateOfBirth(java.time.LocalDate.of(2000,1,1));
            student.setPlaceOfBirth("TestCity");
            studentRepository.save(student);
        }
    }
}
