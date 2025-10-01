package com.university.ManageNotes.data;

import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Roles;
import com.university.ManageNotes.model.Student;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.AppRole;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.enums.StudentLevel;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.RoleRepository;
import com.university.ManageNotes.repository.StudentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.TeacherRepository;
import com.university.ManageNotes.repository.TeachingLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

// @Component
@RequiredArgsConstructor
@Order(3)
public class StudentTeacherDataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final TeachingLevelRepository teachingLevelRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            long studentCount = studentRepository.count();
            long teacherCount = teacherRepository.count();
            
            System.out.println("📊 Current data: Students=" + studentCount + ", Teachers=" + teacherCount);
            
            if (studentCount == 0 || teacherCount == 0) {
                System.out.println("🚀 Starting student and teacher initialization...");
                initializeTeachingLevels();
                initializeTeachers();
                initializeStudents();
                assignSubjectsToTeachers();
                System.out.println("✅ Student and teacher initialization completed");
            } else {
                System.out.println("🔄 Ensuring all teachers have subjects assigned...");
                assignSubjectsToTeachers();
            }
        } catch (Exception e) {
            System.err.println("❌ Error in StudentTeacherDataInitializer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeTeachingLevels() {
        System.out.println("🎯 Initializing teaching levels...");
        for (StudentLevel level : StudentLevel.values()) {
            if (!teachingLevelRepository.existsByStudentLevel(level)) {
                TeachingLevel teachingLevel = new TeachingLevel();
                teachingLevel.setStudentLevel(level);
                teachingLevelRepository.save(teachingLevel);
                System.out.println("✅ Created teaching level: " + level);
            } else {
                System.out.println("ℹ️ Teaching level already exists: " + level);
            }
        }
    }

    private void initializeTeachers() {
        Roles teacherRole = getOrCreateTeacherRole();
        List<Department> departments = departmentRepository.findAll();
        
        for (Department dept : departments) {
            createTeachersForDepartment(dept, teacherRole);
        }
    }

    private void createTeachersForDepartment(Department dept, Roles teacherRole) {
        switch (dept.getDepartmentName()) {
            case "Computer Science":
                createTeacher("prof.smith", "John", "Smith", "john.smith@university.edu", 
                            "123456789", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.johnson", "Sarah", "Johnson", "sarah.johnson@university.edu", 
                            "123456790", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                createTeacher("prof.williams", "Michael", "Williams", "michael.williams@university.edu", 
                            "123456791", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL3, StudentLevel.LEVEL4));
                createTeacher("prof.brown", "Emily", "Brown", "emily.brown@university.edu", 
                            "123456792", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL4, StudentLevel.LEVEL5));
                break;
                
            case "Mathematics":
                createTeacher("prof.davis", "Robert", "Davis", "robert.davis@university.edu", 
                            "123456793", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.miller", "Jennifer", "Miller", "jennifer.miller@university.edu", 
                            "123456794", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                createTeacher("prof.wilson", "David", "Wilson", "david.wilson@university.edu", 
                            "123456795", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL4, StudentLevel.LEVEL5));
                break;
                
            case "Physics":
                createTeacher("prof.moore", "Lisa", "Moore", "lisa.moore@university.edu", 
                            "123456796", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.taylor", "James", "Taylor", "james.taylor@university.edu", 
                            "123456797", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                createTeacher("prof.anderson", "Maria", "Anderson", "maria.anderson@university.edu", 
                            "123456798", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL4, StudentLevel.LEVEL5));
                break;
                
            case "Business Administration":
                createTeacher("prof.thomas", "Christopher", "Thomas", "christopher.thomas@university.edu", 
                            "123456799", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.jackson", "Amanda", "Jackson", "amanda.jackson@university.edu", 
                            "123456800", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                createTeacher("prof.white", "Richard", "White", "richard.white@university.edu", 
                            "123456801", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL4, StudentLevel.LEVEL5));
                break;
                
            case "Engineering":
                createTeacher("prof.harris", "Michelle", "Harris", "michelle.harris@university.edu", 
                            "123456802", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.martin", "Kevin", "Martin", "kevin.martin@university.edu", 
                            "123456803", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                createTeacher("prof.garcia", "Carlos", "Garcia", "carlos.garcia@university.edu", 
                            "123456804", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL4, StudentLevel.LEVEL5));
                break;
        }
    }

    private void createTeacher(String username, String firstName, String lastName, String email, 
                              String phone, Department dept, Roles role, List<StudentLevel> levels) {
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setPassword(passwordEncoder.encode("duchelle"));
        teacher.setEmail(email);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setPhoneNumber(phone);
        teacher.setDepartment(dept);
        teacher.setRole(role);
        teacher.setIsActive(true);
        teacher.setCreatedDate(Instant.now());
        teacher.setLastModifiedDate(Instant.now());
        
        // Set teaching levels for teacher
        List<TeachingLevel> teachingLevels = new ArrayList<>();
        for (StudentLevel level : levels) {
            Optional<TeachingLevel> teachingLevel = teachingLevelRepository.findByStudentLevel(level);
            if (teachingLevel.isPresent()) {
                teachingLevels.add(teachingLevel.get());
                System.out.println("✅ Assigned level " + level + " to teacher " + username);
            } else {
                System.out.println("⚠️ Teaching level not found: " + level + " for teacher " + username);
            }
        }
        teacher.setTeachingLevels(teachingLevels);
        
        teacherRepository.save(teacher);
        System.out.println("✅ Created teacher: " + username + " with " + teachingLevels.size() + " teaching levels");
    }

    private void assignSubjectsToTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<Subject> allSubjects = subjectRepository.findAll();
        List<TeachingLevel> allLevels = teachingLevelRepository.findAll();
        
        System.out.println("🎯 Assigning subjects to " + teachers.size() + " teachers...");
        System.out.println("📚 Found " + allSubjects.size() + " subjects and " + allLevels.size() + " teaching levels");
        
        // First, assign teaching levels to subjects based on their codes
        for (Subject subject : allSubjects) {
            if (subject.getSubjectLevel() == null) {
                TeachingLevel matchingLevel = findMatchingLevelForSubject(subject, allLevels);
                if (matchingLevel != null) {
                    subject.setSubjectLevel(matchingLevel);
                    subjectRepository.save(subject);
                    System.out.println("🎯 Assigned level " + matchingLevel.getStudentLevel() + " to subject " + subject.getSubjectName());
                }
            }
        }
        
        // Then assign teachers to subjects
        for (Teacher teacher : teachers) {
            List<Subject> deptSubjects = allSubjects.stream()
                .filter(s -> s.getDepartment().equals(teacher.getDepartment()))
                .filter(s -> s.getSubjectLevel() != null)
                .toList();
            
            System.out.println("👨🏫 Processing teacher: " + teacher.getUsername() + " with " + teacher.getTeachingLevels().size() + " teaching levels");
            
            // Assign exactly one subject per level the teacher teaches
            for (TeachingLevel teachingLevel : teacher.getTeachingLevels()) {
                // Check if teacher already has a subject for this level
                boolean hasSubjectForLevel = teacher.getSubjects() != null && 
                    teacher.getSubjects().stream()
                        .anyMatch(s -> s.getSubjectLevel() != null && 
                                 s.getSubjectLevel().getStudentLevel().equals(teachingLevel.getStudentLevel()));
                
                if (!hasSubjectForLevel) {
                    Optional<Subject> availableSubject = deptSubjects.stream()
                        .filter(s -> s.getTeacher() == null)
                        .filter(s -> s.getSubjectLevel().getStudentLevel().equals(teachingLevel.getStudentLevel()))
                        .findFirst();
                        
                    if (availableSubject.isPresent()) {
                        Subject subject = availableSubject.get();
                        subject.setTeacher(teacher);
                        
                        // Update bidirectional relationship
                        if (teacher.getSubjects() == null) {
                            teacher.setSubjects(new ArrayList<>());
                        }
                        teacher.getSubjects().add(subject);
                        
                        subjectRepository.save(subject);
                        System.out.println("✅ Assigned " + subject.getSubjectName() + " to " + teacher.getUsername() + " at level " + teachingLevel.getStudentLevel());
                    } else {
                        System.out.println("⚠️ No available subject found for teacher " + teacher.getUsername() + " at level " + teachingLevel.getStudentLevel());
                    }
                } else {
                    System.out.println("ℹ️ Teacher " + teacher.getUsername() + " already has a subject for level " + teachingLevel.getStudentLevel());
                }
            }
            
            // Save teacher with updated subjects
            teacherRepository.save(teacher);
            System.out.println("💾 Saved teacher " + teacher.getUsername() + " with " + 
                (teacher.getSubjects() != null ? teacher.getSubjects().size() : 0) + " subjects");
        }
        
        System.out.println("✅ Subject assignment completed");
    }

    private TeachingLevel findMatchingLevelForSubject(Subject subject, List<TeachingLevel> allLevels) {
        String code = subject.getSubjectCode();
        if (code == null) return null;
        
        StudentLevel targetLevel;
        
        if (code.contains("101") || code.contains("111") || code.contains("121") || 
            code.contains("131") || code.contains("141") || code.contains("102") || 
            code.contains("112") || code.contains("113") || code.contains("114") ||
            code.contains("142") || code.contains("143") || code.contains("144")) {
            targetLevel = StudentLevel.LEVEL1;
        } else if (code.contains("201") || code.contains("211") || code.contains("221") || 
                   code.contains("231") || code.contains("241") || code.contains("202") || 
                   code.contains("203") || code.contains("204") || code.contains("212") || 
                   code.contains("213") || code.contains("214") || code.contains("215") ||
                   code.contains("242") || code.contains("243")) {
            targetLevel = StudentLevel.LEVEL2;
        } else if (code.contains("301") || code.contains("311") || code.contains("321") || 
                   code.contains("331") || code.contains("341") || code.contains("312")) {
            targetLevel = StudentLevel.LEVEL3;
        } else if (code.contains("401") || code.contains("411") || code.contains("421") || 
                   code.contains("431") || code.contains("441") || code.contains("412")) {
            targetLevel = StudentLevel.LEVEL4;
        } else if (code.contains("501") || code.contains("511") || code.contains("521") || 
                   code.contains("531") || code.contains("541") || code.contains("512")) {
            targetLevel = StudentLevel.LEVEL5;
        } else {
            return null;
        }
        
        final StudentLevel finalTargetLevel = targetLevel;
        return allLevels.stream()
            .filter(level -> level.getStudentLevel().equals(finalTargetLevel))
            .findFirst()
            .orElse(null);
    }

    private void initializeStudents() {
        Roles studentRole = getOrCreateStudentRole();
        
        // Create students for each level and cycle
        createStudentsForLevel(StudentLevel.LEVEL1, StudentCycle.BACHELOR, studentRole, 15);
        createStudentsForLevel(StudentLevel.LEVEL2, StudentCycle.BACHELOR, studentRole, 12);
        createStudentsForLevel(StudentLevel.LEVEL3, StudentCycle.BACHELOR, studentRole, 10);
        createStudentsForLevel(StudentLevel.LEVEL4, StudentCycle.MASTER, studentRole, 8);
        createStudentsForLevel(StudentLevel.LEVEL5, StudentCycle.MASTER, studentRole, 6);
    }

    private void createStudentsForLevel(StudentLevel level, StudentCycle cycle, Roles role, int count) {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah", 
                              "Ivan", "Julia", "Kevin", "Laura", "Michael", "Nina", "Oscar", "Paula"};
        String[] lastNames = {"Anderson", "Brown", "Clark", "Davis", "Evans", "Foster", "Green", "Hall", 
                             "Jackson", "King", "Lewis", "Miller", "Nelson", "Parker", "Roberts", "Smith"};
        String[] specialities = {"Computer Science", "Software Engineering", "Data Science", "Mathematics", 
                               "Applied Mathematics", "Physics", "Business Administration", "Engineering"};
        String[] cities = {"Yaoundé", "Douala", "Bamenda", "Bafoussam", "Garoua", "Maroua", "Ngaoundéré", 
                          "Bertoua", "Ebolowa", "Kribi", "Limbe", "Buea", "Kumba", "Dschang", "Foumban"};

        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String matricule = generateMatricule(level, i);
            String speciality = specialities[random.nextInt(specialities.length)];
            String city = cities[random.nextInt(cities.length)];
            
            createStudent(firstName, lastName, matricule, speciality, city, level, cycle, role);
        }
    }

    private String generateMatricule(StudentLevel level, int index) {
        String year = "24";
        String levelCode = switch (level) {
            case LEVEL1 -> "A";
            case LEVEL2 -> "B";
            case LEVEL3 -> "C";
            case LEVEL4 -> "D";
            case LEVEL5 -> "E";
        };
        return String.format("%s%s%04d", year, levelCode, index + 1);
    }

    private void createStudent(String firstName, String lastName, String matricule, String speciality, 
                              String placeOfBirth, StudentLevel level, StudentCycle cycle, Roles role) {
        Student student = new Student();
        student.setUsername(matricule.toLowerCase());
        student.setPassword(passwordEncoder.encode("nathan"));
        student.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "." + matricule.toLowerCase() + "@student.university.edu");
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setMatricule(matricule);
        student.setSpeciality(speciality);
        student.setPlaceOfBirth(placeOfBirth);
        student.setCycle(cycle);
        student.setRole(role);
        student.setIsActive(true);
        student.setCreatedDate(Instant.now());
        student.setLastModifiedDate(Instant.now());
        
        // Generate random birth date (18-25 years old)
        Random random = new Random();
        int age = 18 + random.nextInt(8);
        LocalDate birthDate = LocalDate.now().minusYears(age).minusDays(random.nextInt(365));
        student.setDateOfBirth(birthDate);
        
        // Set student level - ensure it exists
        TeachingLevel studentLevel = teachingLevelRepository.findByStudentLevel(level)
            .orElseThrow(() -> new RuntimeException("TeachingLevel not found for: " + level));
        student.setStudentLevel(studentLevel);
        
        studentRepository.save(student);
    }

    private Roles getOrCreateTeacherRole() {
        return roleRepository.findByAppRole(AppRole.TEACHER)
            .orElseGet(() -> {
                Roles teacherRole = new Roles();
                teacherRole.setAppRole(AppRole.TEACHER);
                return roleRepository.save(teacherRole);
            });
    }

    private Roles getOrCreateStudentRole() {
        return roleRepository.findByAppRole(AppRole.STUDENT)
            .orElseGet(() -> {
                Roles studentRole = new Roles();
                studentRole.setAppRole(AppRole.STUDENT);
                return roleRepository.save(studentRole);
            });
    }
}