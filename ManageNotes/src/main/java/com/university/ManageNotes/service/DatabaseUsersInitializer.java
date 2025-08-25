package com.university.ManageNotes.service;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DatabaseUsersInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createTeachers();
        createStudents();
    }

    private void createTeachers() {
        // Create teachers if they don't exist
        createTeacherIfNotExists("prof.johnson", "Michael", "Johnson", "mjohnson@university.edu", "Computer Science", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2", "LEVEL3", "LEVEL4")));
        createTeacherIfNotExists("prof.williams", "Sarah", "Williams", "swilliams@university.edu", "Mathematics", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.brown", "David", "Brown", "dbrown@university.edu", "Physics", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.davis", "Emily", "Davis", "edavis@university.edu", "Engineering", new ArrayList<>(Arrays.asList("LEVEL3", "LEVEL4")));
    }

    private void createStudents() {
        // Create student users and corresponding student records
        createStudentIfNotExists("alice.cooper", "Alice", "Cooper", "alice.cooper@student.university.edu", "STU2024001", StudentLevel.LEVEL1, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2005, 3, 15), "Yaoundé");
        createStudentIfNotExists("bob.martin", "Bob", "Martin", "bob.martin@student.university.edu", "STU2024002", StudentLevel.LEVEL1, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2005, 7, 22), "Douala");
        createStudentIfNotExists("carol.white", "Carol", "White", "carol.white@student.university.edu", "STU2024003", StudentLevel.LEVEL1, "Physics", StudentCycle.BACHELOR, LocalDate.of(2005, 1, 10), "Bamenda");
        createStudentIfNotExists("daniel.green", "Daniel", "Green", "daniel.green@student.university.edu", "STU2023001", StudentLevel.LEVEL2, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2004, 4, 12), "Yaoundé");
        createStudentIfNotExists("eva.black", "Eva", "Black", "eva.black@student.university.edu", "STU2023002", StudentLevel.LEVEL2, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2004, 8, 30), "Douala");
        createStudentIfNotExists("frank.blue", "Frank", "Blue", "frank.blue@student.university.edu", "STU2023003", StudentLevel.LEVEL2, "Physics", StudentCycle.BACHELOR, LocalDate.of(2004, 2, 14), "Bamenda");
        createStudentIfNotExists("grace.red", "Grace", "Red", "grace.red@student.university.edu", "STU2022001", StudentLevel.LEVEL3, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2003, 5, 20), "Yaoundé");
        createStudentIfNotExists("henry.yellow", "Henry", "Yellow", "henry.yellow@student.university.edu", "STU2022002", StudentLevel.LEVEL3, "Engineering", StudentCycle.BACHELOR, LocalDate.of(2003, 9, 15), "Douala");
        createStudentIfNotExists("iris.purple", "Iris", "Purple", "iris.purple@student.university.edu", "STU2021001", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 1, 15), "Yaoundé");
        createStudentIfNotExists("jack.orange", "Jack", "Orange", "jack.orange@student.university.edu", "STU2021002", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 5, 22), "Douala");
    }

    private void createTeacherIfNotExists(String username, String firstName, String lastName, String email, String department, java.util.List<String> levels) {
        Users teacher = userRepository.findByUsername(username).orElse(new Users());
        
        teacher.setUsername(username);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setPassword(passwordEncoder.encode("nathan"));
        teacher.setRole(Role.TEACHER);
        teacher.setActive(true);
        teacher.setMustChangePassword(false);
        teacher.setDepartment(department);
        teacher.setLevels(levels);
        teacher.setPhone("+237123456789");
        userRepository.save(teacher);
        System.out.println("Teacher " + username + " created/updated with password: nathan");
    }

    private void createStudentIfNotExists(String username, String firstName, String lastName, String email, String matricule, StudentLevel level, String speciality, StudentCycle cycle, LocalDate dateOfBirth, String placeOfBirth) {
        // Create/update user account
        Users studentUser = userRepository.findByUsername(username).orElse(new Users());
        
        studentUser.setUsername(username);
        studentUser.setFirstName(firstName);
        studentUser.setLastName(lastName);
        studentUser.setEmail(email);
        studentUser.setPassword(passwordEncoder.encode("nathan"));
        studentUser.setRole(Role.STUDENT);
        studentUser.setActive(true);
        studentUser.setMustChangePassword(false);
        userRepository.save(studentUser);
        System.out.println("Student " + username + " created/updated with password: nathan");

        // Create student record if it doesn't exist
        if (studentRepository.findByMatricule(matricule).isEmpty()) {
            Students student = new Students();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setMatricule(matricule);
            student.setLevel(level);
            student.setSpeciality(speciality);
            student.setCycle(cycle);
            student.setDateOfBirth(dateOfBirth);
            student.setPlaceOfBirth(placeOfBirth);
            studentRepository.save(student);
        }
    }
}