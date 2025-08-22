package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TeacherInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("teacher").isEmpty()) {
            Users teacher = new Users();
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

            // Assign teacher to existing subjects without teacher
            var subjects = subjectRepository.findAll();
            subjects.stream().filter(s -> s.getIdTeacher() == null).forEach(s -> {
                s.setIdTeacher(teacher.getId());
                subjectRepository.save(s);
            });
        }
    }
}
