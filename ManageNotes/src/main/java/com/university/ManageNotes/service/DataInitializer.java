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
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        int index = 1;
        for (StudentLevel level : StudentLevel.values()) {
            String username = "student" + index;
            if (userRepository.findByUsername(username).isPresent()) {
                index++;
                continue;
            }
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.STUDENT);
            user.setEmail(username + "@example.com");
            user.setFirstName("Student" + index);
            user.setLastName("Level" + level.getValue());
            user.setActive(true);
            Users savedUser = userRepository.save(user);

            Students student = new Students();
            student.setFirstName(savedUser.getFirstName());
            student.setLastName(savedUser.getLastName());
            student.setEmail(savedUser.getEmail());
            student.setMatricule("MAT" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            student.setLevel(level);
            student.setCycle(level.getValue() <= 3 ? StudentCycle.BACHELOR : StudentCycle.MASTER);
            studentRepository.save(student);
            index++;
        }
    }
}
