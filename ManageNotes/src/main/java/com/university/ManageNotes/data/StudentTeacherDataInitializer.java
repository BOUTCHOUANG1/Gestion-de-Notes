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

@Component
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
                System.out.println("🔄 Checking subject assignments...");
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
                            "123456791", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL3));
                break;
                
            case "Mathematics":
                createTeacher("prof.davis", "Robert", "Davis", "robert.davis@university.edu", 
                            "123456793", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.miller", "Jennifer", "Miller", "jennifer.miller@university.edu", 
                            "123456794", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                break;
                
            case "Physics":
                createTeacher("prof.moore", "Lisa", "Moore", "lisa.moore@university.edu", 
                            "123456796", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.taylor", "James", "Taylor", "james.taylor@university.edu", 
                            "123456797", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                break;
                
            case "Business Administration":
                createTeacher("prof.thomas", "Christopher", "Thomas", "christopher.thomas@university.edu", 
                            "123456799", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.jackson", "Amanda", "Jackson", "amanda.jackson@university.edu", 
                            "123456800", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
                break;
                
            case "Engineering":
                createTeacher("prof.harris", "Michelle", "Harris", "michelle.harris@university.edu", 
                            "123456802", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL1, StudentLevel.LEVEL2));
                createTeacher("prof.martin", "Kevin", "Martin", "kevin.martin@university.edu", 
                            "123456803", dept, teacherRole, Arrays.asList(StudentLevel.LEVEL2, StudentLevel.LEVEL3));
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
        
        for (Teacher teacher : teachers) {
            List<Subject> deptSubjects = allSubjects.stream()
                .filter(s -> s.getDepartment().equals(teacher.getDepartment()))
                .toList();
            
            // Assign subjects based on level matching
            for (TeachingLevel teachingLevel : teacher.getTeachingLevels()) {
                Optional<Subject> availableSubject = deptSubjects.stream()
                    .filter(s -> s.getTeacher() == null)
                    .filter(s -> subjectMatchesLevel(s, teachingLevel.getStudentLevel()))
                    .findFirst();
                    
                if (availableSubject.isPresent()) {
                    Subject subject = availableSubject.get();
                    subject.setTeacher(teacher);
                    subject.setSubjectLevel(teachingLevel);
                    subjectRepository.save(subject);
                    System.out.println("✅ Assigned " + subject.getSubjectName() + " to " + teacher.getUsername() + " at level " + teachingLevel.getStudentLevel());
                } else {
                    System.out.println("⚠️ No available subject found for teacher " + teacher.getUsername() + " at level " + teachingLevel.getStudentLevel());
                }
            }
        }
    }

    private boolean subjectMatchesLevel(Subject subject, StudentLevel level) {
        String code = subject.getSubjectCode();
        if (code == null) return false;
        
        return switch (level) {
            case LEVEL1 -> code.contains("101") || code.contains("111") || code.contains("121") || code.contains("131") || code.contains("141");
            case LEVEL2 -> code.contains("201") || code.contains("211") || code.contains("221") || code.contains("231") || code.contains("241");
            case LEVEL3 -> code.contains("301") || code.contains("311") || code.contains("321") || code.contains("331") || code.contains("341");
            case LEVEL4 -> code.contains("401") || code.contains("411") || code.contains("421") || code.contains("431") || code.contains("441");
            case LEVEL5 -> code.contains("501") || code.contains("511") || code.contains("521") || code.contains("531") || code.contains("541");
        };
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