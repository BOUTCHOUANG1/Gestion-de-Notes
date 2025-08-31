package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// @Component
@RequiredArgsConstructor
public class TeacherInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final SubjectRepository subjectRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Users teacher = userRepository.findByUsername("teacher").orElse(new Users());
        
        teacher.setUsername("teacher");
        teacher.setPassword(passwordEncoder.encode("teacher"));
        teacher.setRole(Role.TEACHER);
        teacher.setEmail("teacher@example.com");
        teacher.setFirstName("Default");
        teacher.setLastName("Teacher");
        teacher.setActive(true);
        teacher.setMustChangePassword(false);
        teacher.setPhone("+1234567890");
        teacher.setDepartment("Mathematics");
        userRepository.save(teacher);
        System.out.println("Teacher user created/updated with password: teacher");

        // Assign teacher to existing subjects without teacher
        var subjects = subjectRepository.findAll();
        subjects.stream().filter(s -> s.getIdTeacher() == null).forEach(s -> {
            s.setIdTeacher(teacher.getId());
            subjectRepository.save(s);
        });
    }
}
