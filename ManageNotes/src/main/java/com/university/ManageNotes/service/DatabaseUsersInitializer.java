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

// @Component
@RequiredArgsConstructor
@org.springframework.core.annotation.Order(2)
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
        // Create all database teachers
        createTeacherIfNotExists("prof.johnson", "Michael", "Johnson", "mjohnson@university.edu", "Computer Science", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2", "LEVEL3", "LEVEL4")));
        createTeacherIfNotExists("prof.williams", "Sarah", "Williams", "swilliams@university.edu", "Mathematics", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.brown", "David", "Brown", "dbrown@university.edu", "Physics", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.davis", "Emily", "Davis", "edavis@university.edu", "Engineering", new ArrayList<>(Arrays.asList("LEVEL3", "LEVEL4")));
        createTeacherIfNotExists("prof.wilson", "Robert", "Wilson", "rwilson@university.edu", "Business Administration", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.garcia", "Maria", "Garcia", "mgarcia@university.edu", "Computer Science", new ArrayList<>(Arrays.asList("LEVEL2", "LEVEL3", "LEVEL4")));
        createTeacherIfNotExists("prof.martinez", "Carlos", "Martinez", "cmartinez@university.edu", "Mathematics", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2")));
        createTeacherIfNotExists("prof.anderson", "Lisa", "Anderson", "landerson@university.edu", "Physics", new ArrayList<>(Arrays.asList("LEVEL2", "LEVEL3")));
        createTeacherIfNotExists("prof.taylor", "James", "Taylor", "jtaylor@university.edu", "Engineering", new ArrayList<>(Arrays.asList("LEVEL3", "LEVEL4")));
        createTeacherIfNotExists("prof.thomas", "Jennifer", "Thomas", "jthomas@university.edu", "Business Administration", new ArrayList<>(Arrays.asList("LEVEL1", "LEVEL2", "LEVEL3")));
    }

    private void createStudents() {
        // LEVEL1 Students (15 students)
        createStudentIfNotExists("STU2024001", "Alice", "Cooper", "alice.cooper@student.university.edu", "STU2024001", StudentLevel.LEVEL1, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2005, 3, 15), "Yaoundé");
        createStudentIfNotExists("STU2024002", "Bob", "Martin", "bob.martin@student.university.edu", "STU2024002", StudentLevel.LEVEL1, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2005, 7, 22), "Douala");
        createStudentIfNotExists("STU2024003", "Carol", "White", "carol.white@student.university.edu", "STU2024003", StudentLevel.LEVEL1, "Physics", StudentCycle.BACHELOR, LocalDate.of(2005, 1, 10), "Bamenda");
        createStudentIfNotExists("STU2024004", "David", "Green", "david.green@student.university.edu", "STU2024004", StudentLevel.LEVEL1, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2005, 4, 12), "Yaoundé");
        createStudentIfNotExists("STU2024005", "Eva", "Black", "eva.black@student.university.edu", "STU2024005", StudentLevel.LEVEL1, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2005, 8, 30), "Douala");
        createStudentIfNotExists("STU2024006", "Frank", "Blue", "frank.blue@student.university.edu", "STU2024006", StudentLevel.LEVEL1, "Physics", StudentCycle.BACHELOR, LocalDate.of(2005, 2, 14), "Bamenda");
        createStudentIfNotExists("STU2024007", "Grace", "Red", "grace.red@student.university.edu", "STU2024007", StudentLevel.LEVEL1, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2005, 5, 20), "Yaoundé");
        createStudentIfNotExists("STU2024008", "Henry", "Yellow", "henry.yellow@student.university.edu", "STU2024008", StudentLevel.LEVEL1, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2005, 9, 15), "Douala");
        createStudentIfNotExists("STU2024009", "Iris", "Purple", "iris.purple@student.university.edu", "STU2024009", StudentLevel.LEVEL1, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2005, 1, 15), "Yaoundé");
        createStudentIfNotExists("STU2024010", "Jack", "Orange", "jack.orange@student.university.edu", "STU2024010", StudentLevel.LEVEL1, "Physics", StudentCycle.BACHELOR, LocalDate.of(2005, 5, 22), "Douala");
        createStudentIfNotExists("STU2024011", "Kate", "Brown", "kate.brown@student.university.edu", "STU2024011", StudentLevel.LEVEL1, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2005, 3, 8), "Bamenda");
        createStudentIfNotExists("STU2024012", "Leo", "Gray", "leo.gray@student.university.edu", "STU2024012", StudentLevel.LEVEL1, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2005, 6, 18), "Yaoundé");
        createStudentIfNotExists("STU2024013", "Mia", "Silver", "mia.silver@student.university.edu", "STU2024013", StudentLevel.LEVEL1, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2005, 10, 25), "Douala");
        createStudentIfNotExists("STU2024014", "Noah", "Gold", "noah.gold@student.university.edu", "STU2024014", StudentLevel.LEVEL1, "Physics", StudentCycle.BACHELOR, LocalDate.of(2005, 4, 3), "Bamenda");
        createStudentIfNotExists("STU2024015", "Olivia", "Pink", "olivia.pink@student.university.edu", "STU2024015", StudentLevel.LEVEL1, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2005, 7, 11), "Yaoundé");
        
        // LEVEL2 Students (15 students)
        createStudentIfNotExists("STU2023001", "Paul", "Cyan", "paul.cyan@student.university.edu", "STU2023001", StudentLevel.LEVEL2, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2004, 3, 15), "Yaoundé");
        createStudentIfNotExists("STU2023002", "Quinn", "Magenta", "quinn.magenta@student.university.edu", "STU2023002", StudentLevel.LEVEL2, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2004, 7, 22), "Douala");
        createStudentIfNotExists("STU2023003", "Ruby", "Lime", "ruby.lime@student.university.edu", "STU2023003", StudentLevel.LEVEL2, "Physics", StudentCycle.BACHELOR, LocalDate.of(2004, 1, 10), "Bamenda");
        createStudentIfNotExists("STU2023004", "Sam", "Teal", "sam.teal@student.university.edu", "STU2023004", StudentLevel.LEVEL2, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2004, 4, 12), "Yaoundé");
        createStudentIfNotExists("STU2023005", "Tina", "Coral", "tina.coral@student.university.edu", "STU2023005", StudentLevel.LEVEL2, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2004, 8, 30), "Douala");
        createStudentIfNotExists("STU2023006", "Uma", "Ivory", "uma.ivory@student.university.edu", "STU2023006", StudentLevel.LEVEL2, "Physics", StudentCycle.BACHELOR, LocalDate.of(2004, 2, 14), "Bamenda");
        createStudentIfNotExists("STU2023007", "Victor", "Jade", "victor.jade@student.university.edu", "STU2023007", StudentLevel.LEVEL2, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2004, 5, 20), "Yaoundé");
        createStudentIfNotExists("STU2023008", "Wendy", "Ruby", "wendy.ruby@student.university.edu", "STU2023008", StudentLevel.LEVEL2, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2004, 9, 15), "Douala");
        createStudentIfNotExists("STU2023009", "Xander", "Amber", "xander.amber@student.university.edu", "STU2023009", StudentLevel.LEVEL2, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2004, 1, 15), "Yaoundé");
        createStudentIfNotExists("STU2023010", "Yara", "Pearl", "yara.pearl@student.university.edu", "STU2023010", StudentLevel.LEVEL2, "Physics", StudentCycle.BACHELOR, LocalDate.of(2004, 5, 22), "Douala");
        createStudentIfNotExists("STU2023011", "Zoe", "Onyx", "zoe.onyx@student.university.edu", "STU2023011", StudentLevel.LEVEL2, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2004, 3, 8), "Bamenda");
        createStudentIfNotExists("STU2023012", "Adam", "Copper", "adam.copper@student.university.edu", "STU2023012", StudentLevel.LEVEL2, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2004, 6, 18), "Yaoundé");
        createStudentIfNotExists("STU2023013", "Bella", "Bronze", "bella.bronze@student.university.edu", "STU2023013", StudentLevel.LEVEL2, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2004, 10, 25), "Douala");
        createStudentIfNotExists("STU2023014", "Chris", "Steel", "chris.steel@student.university.edu", "STU2023014", StudentLevel.LEVEL2, "Physics", StudentCycle.BACHELOR, LocalDate.of(2004, 4, 3), "Bamenda");
        createStudentIfNotExists("STU2023015", "Diana", "Platinum", "diana.platinum@student.university.edu", "STU2023015", StudentLevel.LEVEL2, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2004, 7, 11), "Yaoundé");
        
        // LEVEL3 Students (10 students)
        createStudentIfNotExists("STU2022001", "Ethan", "Diamond", "ethan.diamond@student.university.edu", "STU2022001", StudentLevel.LEVEL3, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2003, 3, 15), "Yaoundé");
        createStudentIfNotExists("STU2022002", "Fiona", "Emerald", "fiona.emerald@student.university.edu", "STU2022002", StudentLevel.LEVEL3, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2003, 7, 22), "Douala");
        createStudentIfNotExists("STU2022003", "George", "Sapphire", "george.sapphire@student.university.edu", "STU2022003", StudentLevel.LEVEL3, "Physics", StudentCycle.BACHELOR, LocalDate.of(2003, 1, 10), "Bamenda");
        createStudentIfNotExists("STU2022004", "Hannah", "Topaz", "hannah.topaz@student.university.edu", "STU2022004", StudentLevel.LEVEL3, "Engineering", StudentCycle.BACHELOR, LocalDate.of(2003, 4, 12), "Yaoundé");
        createStudentIfNotExists("STU2022005", "Ian", "Garnet", "ian.garnet@student.university.edu", "STU2022005", StudentLevel.LEVEL3, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2003, 8, 30), "Douala");
        createStudentIfNotExists("STU2022006", "Julia", "Opal", "julia.opal@student.university.edu", "STU2022006", StudentLevel.LEVEL3, "Mathematics", StudentCycle.BACHELOR, LocalDate.of(2003, 2, 14), "Bamenda");
        createStudentIfNotExists("STU2022007", "Kevin", "Quartz", "kevin.quartz@student.university.edu", "STU2022007", StudentLevel.LEVEL3, "Physics", StudentCycle.BACHELOR, LocalDate.of(2003, 5, 20), "Yaoundé");
        createStudentIfNotExists("STU2022008", "Luna", "Agate", "luna.agate@student.university.edu", "STU2022008", StudentLevel.LEVEL3, "Engineering", StudentCycle.BACHELOR, LocalDate.of(2003, 9, 15), "Douala");
        createStudentIfNotExists("STU2022009", "Max", "Jasper", "max.jasper@student.university.edu", "STU2022009", StudentLevel.LEVEL3, "Computer Science", StudentCycle.BACHELOR, LocalDate.of(2003, 1, 15), "Yaoundé");
        createStudentIfNotExists("STU2022010", "Nina", "Turquoise", "nina.turquoise@student.university.edu", "STU2022010", StudentLevel.LEVEL3, "Business Administration", StudentCycle.BACHELOR, LocalDate.of(2003, 5, 22), "Douala");
        
        // LEVEL4 Students (10 students)
        createStudentIfNotExists("STU2021001", "Oscar", "Obsidian", "oscar.obsidian@student.university.edu", "STU2021001", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 3, 15), "Yaoundé");
        createStudentIfNotExists("STU2021002", "Penny", "Peridot", "penny.peridot@student.university.edu", "STU2021002", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 7, 22), "Douala");
        createStudentIfNotExists("STU2021003", "Quinn", "Citrine", "quinn.citrine@student.university.edu", "STU2021003", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 1, 10), "Bamenda");
        createStudentIfNotExists("STU2021004", "Rose", "Amethyst", "rose.amethyst@student.university.edu", "STU2021004", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 4, 12), "Yaoundé");
        createStudentIfNotExists("STU2021005", "Steve", "Beryl", "steve.beryl@student.university.edu", "STU2021005", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 8, 30), "Douala");
        createStudentIfNotExists("STU2021006", "Tara", "Carnelian", "tara.carnelian@student.university.edu", "STU2021006", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 2, 14), "Bamenda");
        createStudentIfNotExists("STU2021007", "Ulrich", "Fluorite", "ulrich.fluorite@student.university.edu", "STU2021007", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 5, 20), "Yaoundé");
        createStudentIfNotExists("STU2021008", "Vera", "Hematite", "vera.hematite@student.university.edu", "STU2021008", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 9, 15), "Douala");
        createStudentIfNotExists("STU2021009", "Will", "Labradorite", "will.labradorite@student.university.edu", "STU2021009", StudentLevel.LEVEL4, "Computer Science", StudentCycle.MASTER, LocalDate.of(2002, 1, 15), "Yaoundé");
        createStudentIfNotExists("STU2021010", "Zara", "Malachite", "zara.malachite@student.university.edu", "STU2021010", StudentLevel.LEVEL4, "Engineering", StudentCycle.MASTER, LocalDate.of(2002, 5, 22), "Douala");
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