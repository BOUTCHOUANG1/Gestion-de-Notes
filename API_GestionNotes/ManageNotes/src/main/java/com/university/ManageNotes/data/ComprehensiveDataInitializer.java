package com.university.ManageNotes.data;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.*;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

// @Component - Disabled after initial database population
@RequiredArgsConstructor
@Order(1)
public class ComprehensiveDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final TeachingLevelRepository teachingLevelRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final RevendicationPeriodRepository revendicationPeriodRepository;
    private final GradeRepository gradeRepository;
    private final TranscriptRepository transcriptRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if data already exists - run only once
        if (isDataAlreadyInitialized()) {
            System.out.println("ℹ️ Data already initialized, skipping initialization");
            return;
        }
        
        System.out.println("🚀 Starting comprehensive data initialization...");
        
        // Step 1: Initialize basic data
        initializeRoles();
        initializeAdmin();
        initializeSemesters();
        initializeExams();
        initializeRevendicationPeriods();
        initializeTeachingLevels();
        initializeDepartments();
        initializeSubjects();
        
        // Step 2: Initialize users
        initializeTeachers();
        initializeStudents();
        
        // Step 3: Assign subjects to teachers
        assignSubjectsToTeachers();
        
        // Step 4: Generate grades and transcripts
        generateGrades();
        generateTranscripts();
        
        System.out.println("✅ Comprehensive data initialization completed!");
    }
    
    private boolean isDataAlreadyInitialized() {
        return userRepository.count() > 1 && // More than just admin
               departmentRepository.count() > 0 &&
               subjectRepository.count() > 0 &&
               studentRepository.count() > 0 &&
               teacherRepository.count() > 0;
    }

    private void initializeRoles() {
        System.out.println("🎭 Initializing roles...");
        for (AppRole appRole : AppRole.values()) {
            if (!roleRepository.findByAppRole(appRole).isPresent()) {
                Roles role = new Roles();
                role.setAppRole(appRole);
                roleRepository.save(role);
                System.out.println("✅ Created role: " + appRole);
            }
        }
    }

    private void initializeAdmin() {
        System.out.println("👤 Initializing admin user...");
        if (!userRepository.findByUsername("admin").isPresent()) {
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@university.edu");
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setIsActive(true);
            admin.setCreatedDate(Instant.now());
            admin.setLastModifiedDate(Instant.now());
            admin.setRole(roleRepository.findByAppRole(AppRole.ADMIN).get());
            userRepository.save(admin);
            System.out.println("✅ Admin created: admin/admin");
        }
    }

    private void initializeSemesters() {
        System.out.println("📅 Initializing semesters...");
        if (semesterRepository.count() == 0) {
            Semester sem1 = new Semester();
            sem1.setName("Semester 1 - 2024/2025");
            sem1.setStartDate(LocalDate.of(2024, 9, 8));
            sem1.setEndDate(LocalDate.of(2025, 2, 23));
            sem1.setActive(true);
            sem1.setCreatedDate(Instant.now());
            sem1.setLastModifiedDate(Instant.now());
            semesterRepository.save(sem1);

            Semester sem2 = new Semester();
            sem2.setName("Semester 2 - 2024/2025");
            sem2.setStartDate(LocalDate.of(2025, 3, 15));
            sem2.setEndDate(LocalDate.of(2025, 6, 2));
            sem2.setActive(false);
            sem2.setCreatedDate(Instant.now());
            sem2.setLastModifiedDate(Instant.now());
            semesterRepository.save(sem2);
            
            System.out.println("✅ Created 2 semesters");
        }
    }

    private void initializeExams() {
        System.out.println("📝 Initializing exams...");
        if (examRepository.count() == 0) {
            for (AssessmentType type : AssessmentType.values()) {
                Exam exam = new Exam();
                exam.setAssessmentType(type);
                examRepository.save(exam);
            }
            System.out.println("✅ Created 4 exam types");
        }
    }

    private void initializeRevendicationPeriods() {
        System.out.println("⏰ Initializing revendication periods...");
        if (revendicationPeriodRepository.count() == 0) {
            List<Semester> semesters = semesterRepository.findAll();
            List<Exam> exams = examRepository.findAll();
            
            for (Semester semester : semesters) {
                for (Exam exam : exams) {
                    // Only create periods for specific semester-exam combinations
                    boolean shouldCreate = (semester.getName().contains("Semester 1") && 
                                          (exam.getAssessmentType() == AssessmentType.CC_1 || exam.getAssessmentType() == AssessmentType.SN_1)) ||
                                         (semester.getName().contains("Semester 2") && 
                                          (exam.getAssessmentType() == AssessmentType.CC_2 || exam.getAssessmentType() == AssessmentType.SN_2));
                    
                    if (shouldCreate) {
                        RevendicationPeriod period = new RevendicationPeriod();
                        period.setStartDate(semester.getStartDate().plusDays(30));
                        period.setEndDate(semester.getStartDate().plusDays(45));
                        period.setExam(exam);
                        period.setSemester(semester);
                        period.setColor(getColorForAssessmentType(exam.getAssessmentType()));
                        period.setIsActive(true);
                        period.setCreatedDate(Instant.now());
                        period.setLastModifiedDate(Instant.now());
                        revendicationPeriodRepository.save(period);
                    }
                }
            }
            System.out.println("✅ Created revendication periods");
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
            }
        }
    }

    private void initializeDepartments() {
        System.out.println("🏢 Initializing departments...");
        String[] deptNames = {"Computer Science", "Mathematics", "Physics", "Business Administration", "Engineering"};
        
        for (String name : deptNames) {
            if (departmentRepository.findAll().stream().noneMatch(d -> d.getDepartmentName().equals(name))) {
                Department dept = new Department();
                dept.setDepartmentName(name);
                dept.setCreatedDate(Instant.now());
                dept.setLastModifiedDate(Instant.now());
                departmentRepository.save(dept);
                System.out.println("✅ Created department: " + name);
            }
        }
    }

    private void initializeSubjects() {
        System.out.println("📚 Initializing subjects...");
        if (subjectRepository.count() == 0) {
            List<Department> departments = departmentRepository.findAll();
            List<Semester> semesters = semesterRepository.findAll();
            
            for (Department dept : departments) {
                createSubjectsForDepartment(dept, semesters);
            }
            System.out.println("✅ Created subjects for all departments");
        }
    }

    private void createSubjectsForDepartment(Department dept, List<Semester> semesters) {
        Semester sem1 = semesters.get(0);
        Semester sem2 = semesters.get(1);
        
        switch (dept.getDepartmentName()) {
            case "Computer Science":
                createSubject("Programming Fundamentals", "CS101", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Data Structures", "CS201", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR);
                createSubject("Algorithms", "CS301", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Machine Learning", "CS401", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                createSubject("AI Research", "CS501", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                break;
            case "Mathematics":
                createSubject("Calculus I", "MATH111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Calculus II", "MATH211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR);
                createSubject("Real Analysis", "MATH311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Advanced Analysis", "MATH411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                createSubject("Mathematical Research Methods", "MATH511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                break;
            case "Physics":
                createSubject("Classical Mechanics", "PHY111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Electromagnetism", "PHY211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR);
                createSubject("Quantum Mechanics", "PHY311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Advanced Quantum", "PHY411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                createSubject("Physics Research Project", "PHY511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                break;
            case "Business Administration":
                createSubject("Principles of Management", "BUS111", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Marketing Principles", "MKT211", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR);
                createSubject("Strategic Management", "BUS311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Advanced Finance", "FIN411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                createSubject("Business Research Methods", "BUS511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                break;
            case "Engineering":
                createSubject("Engineering Mathematics I", "MATH141", new BigDecimal("7"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Engineering Mathematics II", "MATH241", new BigDecimal("7"), dept, sem2, StudentCycle.BACHELOR);
                createSubject("Control Systems", "ENG341", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR);
                createSubject("Advanced Engineering", "ENG441", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                createSubject("Engineering Research Methods", "ENG541", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER);
                break;
        }
    }

    private void createSubject(String name, String code, BigDecimal credits, Department dept, Semester semester, StudentCycle cycle) {
        Subject subject = new Subject();
        subject.setSubjectName(name);
        subject.setSubjectCode(code);
        subject.setCredits(credits);
        subject.setDepartment(dept);
        subject.setSemester(semester);
        subject.setStudentcycle(cycle);
        subject.setDescription("Description for " + name);
        subject.setTranscript(null); // Explicitly set to null
        subjectRepository.save(subject);
    }

    private void initializeTeachers() {
        System.out.println("👨‍🏫 Initializing teachers...");
        if (teacherRepository.count() == 0) {
            Roles teacherRole = roleRepository.findByAppRole(AppRole.TEACHER).get();
            List<Department> departments = departmentRepository.findAll();
            
            for (Department dept : departments) {
                createTeachersForDepartment(dept, teacherRole);
            }
            System.out.println("✅ Created teachers for all departments");
        }
    }

    private void createTeachersForDepartment(Department dept, Roles teacherRole) {
        switch (dept.getDepartmentName()) {
            case "Computer Science":
                createTeacher("prof.smith", "John", "Smith", "john.smith@university.edu", 
                            "123456789", dept, teacherRole, "LEVEL1,LEVEL2");
                createTeacher("prof.johnson", "Sarah", "Johnson", "sarah.johnson@university.edu", 
                            "123456790", dept, teacherRole, "LEVEL2,LEVEL3");
                createTeacher("prof.williams", "Michael", "Williams", "michael.williams@university.edu", 
                            "123456791", dept, teacherRole, "LEVEL3,LEVEL4");
                createTeacher("prof.brown", "Emily", "Brown", "emily.brown@university.edu", 
                            "123456792", dept, teacherRole, "LEVEL4,LEVEL5");
                break;
                
            case "Mathematics":
                createTeacher("prof.davis", "Robert", "Davis", "robert.davis@university.edu", 
                            "123456793", dept, teacherRole, "LEVEL1,LEVEL2");
                createTeacher("prof.miller", "Jennifer", "Miller", "jennifer.miller@university.edu", 
                            "123456794", dept, teacherRole, "LEVEL2,LEVEL3");
                createTeacher("prof.wilson", "David", "Wilson", "david.wilson@university.edu", 
                            "123456795", dept, teacherRole, "LEVEL4,LEVEL5");
                break;
                
            case "Physics":
                createTeacher("prof.moore", "Lisa", "Moore", "lisa.moore@university.edu", 
                            "123456796", dept, teacherRole, "LEVEL1,LEVEL2");
                createTeacher("prof.taylor", "James", "Taylor", "james.taylor@university.edu", 
                            "123456797", dept, teacherRole, "LEVEL2,LEVEL3");
                createTeacher("prof.anderson", "Maria", "Anderson", "maria.anderson@university.edu", 
                            "123456798", dept, teacherRole, "LEVEL4,LEVEL5");
                break;
                
            case "Business Administration":
                createTeacher("prof.thomas", "Christopher", "Thomas", "christopher.thomas@university.edu", 
                            "123456799", dept, teacherRole, "LEVEL1,LEVEL2");
                createTeacher("prof.jackson", "Amanda", "Jackson", "amanda.jackson@university.edu", 
                            "123456800", dept, teacherRole, "LEVEL2,LEVEL3");
                createTeacher("prof.white", "Richard", "White", "richard.white@university.edu", 
                            "123456801", dept, teacherRole, "LEVEL4,LEVEL5");
                break;
                
            case "Engineering":
                createTeacher("prof.harris", "Michelle", "Harris", "michelle.harris@university.edu", 
                            "123456802", dept, teacherRole, "LEVEL1,LEVEL2");
                createTeacher("prof.martin", "Kevin", "Martin", "kevin.martin@university.edu", 
                            "123456803", dept, teacherRole, "LEVEL2,LEVEL3");
                createTeacher("prof.garcia", "Carlos", "Garcia", "carlos.garcia@university.edu", 
                            "123456804", dept, teacherRole, "LEVEL4,LEVEL5");
                break;
        }
    }

    private void createTeacher(String username, String firstName, String lastName, String email, 
                              String phone, Department dept, Roles role, String levelsStr) {
        // Check if teacher already exists
        if (teacherRepository.findByUsername(username).isPresent()) {
            System.out.println("ℹ️ Teacher already exists: " + username);
            return;
        }
        
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
        
        // Set teaching levels
        List<TeachingLevel> teachingLevels = new ArrayList<>();
        String[] levels = levelsStr.split(",");
        for (String levelStr : levels) {
            StudentLevel level = StudentLevel.valueOf(levelStr);
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
        System.out.println("✅ Created teacher: " + username + " with " + teachingLevels.size() + " levels");
    }

    private void initializeStudents() {
        System.out.println("👨‍🎓 Initializing students...");
        if (studentRepository.count() == 0) {
            Roles studentRole = roleRepository.findByAppRole(AppRole.STUDENT).get();
            
            createStudentsForLevel(StudentLevel.LEVEL1, StudentCycle.BACHELOR, studentRole, 15);
            createStudentsForLevel(StudentLevel.LEVEL2, StudentCycle.BACHELOR, studentRole, 12);
            createStudentsForLevel(StudentLevel.LEVEL3, StudentCycle.BACHELOR, studentRole, 10);
            createStudentsForLevel(StudentLevel.LEVEL4, StudentCycle.MASTER, studentRole, 8);
            createStudentsForLevel(StudentLevel.LEVEL5, StudentCycle.MASTER, studentRole, 6);
            
            System.out.println("✅ Created students for all levels");
        }
    }

    private void createStudentsForLevel(StudentLevel level, StudentCycle cycle, Roles role, int count) {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah", "Ivan", "Julia", "Kevin", "Laura", "Michael", "Nina", "Oscar", "Paula"};
        String[] lastNames = {"Anderson", "Brown", "Clark", "Davis", "Evans", "Foster", "Green", "Hall", "Jackson", "King", "Lewis", "Miller", "Nelson", "Parker", "Roberts", "Smith"};
        String[] specialities = {"Computer Science", "Software Engineering", "Data Science", "Mathematics", "Applied Mathematics", "Physics", "Business Administration", "Engineering"};
        String[] cities = {"Yaoundé", "Douala", "Bamenda", "Bafoussam", "Garoua", "Maroua", "Ngaoundéré", "Bertoua", "Ebolowa", "Kribi", "Limbe", "Buea", "Kumba", "Dschang", "Foumban"};
        
        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String matricule = generateMatricule(level, i);
            String speciality = specialities[random.nextInt(specialities.length)];
            String city = cities[random.nextInt(cities.length)];
            
            // Check if student already exists
            if (studentRepository.findByMatricule(matricule).isPresent()) {
                continue;
            }
            
            Student student = new Student();
            student.setUsername(matricule.toLowerCase());
            student.setPassword(passwordEncoder.encode("nathan"));
            student.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "." + matricule.toLowerCase() + "@student.university.edu");
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setMatricule(matricule);
            student.setSpeciality(speciality);
            student.setPlaceOfBirth(city);
            student.setCycle(cycle);
            student.setRole(role);
            student.setIsActive(true);
            student.setCreatedDate(Instant.now());
            student.setLastModifiedDate(Instant.now());
            student.setDateOfBirth(LocalDate.now().minusYears(18 + random.nextInt(8)));
            
            // Set student level
            Optional<TeachingLevel> teachingLevel = teachingLevelRepository.findByStudentLevel(level);
            if (teachingLevel.isPresent()) {
                student.setStudentLevel(teachingLevel.get());
                studentRepository.save(student);
                System.out.println("✅ Created student: " + matricule + " (" + firstName + " " + lastName + ")");
            } else {
                System.out.println("⚠️ Teaching level not found for: " + level);
            }
        }
    }

    private String generateMatricule(StudentLevel level, int index) {
        String levelCode = switch (level) {
            case LEVEL1 -> "A";
            case LEVEL2 -> "B";
            case LEVEL3 -> "C";
            case LEVEL4 -> "D";
            case LEVEL5 -> "E";
        };
        return String.format("24%s%04d", levelCode, index + 1);
    }

    private void assignSubjectsToTeachers() {
        System.out.println("🎯 Assigning subjects to teachers...");
        
        List<Teacher> teachers = teacherRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<TeachingLevel> levels = teachingLevelRepository.findAll();
        
        // First assign teaching levels to subjects based on their codes
        for (Subject subject : subjects) {
            if (subject.getSubjectLevel() == null) {
                TeachingLevel matchingLevel = findLevelForSubject(subject, levels);
                if (matchingLevel != null) {
                    subject.setSubjectLevel(matchingLevel);
                    subjectRepository.save(subject);
                    System.out.println("🎯 Assigned level " + matchingLevel.getStudentLevel() + " to subject " + subject.getSubjectName());
                }
            }
        }
        
        // Then assign teachers to subjects
        for (Teacher teacher : teachers) {
            List<Subject> deptSubjects = subjects.stream()
                .filter(s -> s.getDepartment().equals(teacher.getDepartment()))
                .filter(s -> s.getSubjectLevel() != null)
                .toList();
            
            System.out.println("👨‍🏫 Processing teacher: " + teacher.getUsername() + " with " + teacher.getTeachingLevels().size() + " teaching levels");
            
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
        

    }

    private TeachingLevel findLevelForSubject(Subject subject, List<TeachingLevel> levels) {
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
        return levels.stream()
            .filter(level -> level.getStudentLevel().equals(finalTargetLevel))
            .findFirst()
            .orElse(null);
    }

    private void generateGrades() {
        System.out.println("📊 Generating grades...");
        
        List<Student> students = studentRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        List<Exam> exams = examRepository.findAll();
        List<Semester> semesters = semesterRepository.findAll();
        Random random = new Random();
        
        int gradeCount = 0;
        for (Student student : students) {
            if (student.getStudentLevel() == null) {
                System.out.println("⚠️ Student " + student.getUsername() + " has no level assigned");
                continue;
            }
            
            List<Subject> studentSubjects = subjects.stream()
                .filter(s -> s.getSubjectLevel() != null && 
                           s.getSubjectLevel().getStudentLevel().equals(student.getStudentLevel().getStudentLevel()))
                .filter(s -> s.getTeacher() != null)
                .toList();
            
            if (studentSubjects.isEmpty()) {
                System.out.println("⚠️ No subjects found for student " + student.getUsername() + " at level " + student.getStudentLevel().getStudentLevel());
                continue;
            }
            
            for (Subject subject : studentSubjects) {
                for (Semester semester : semesters) {
                    for (Exam exam : exams) {
                        // Check if grade already exists
                        if (gradeRepository.existsByStudentAndSubjectAndExamAndSemester(student, subject, exam, semester)) {
                            continue;
                        }
                        
                        createGrade(student, subject, exam, semester, subject.getTeacher(), random);
                        gradeCount++;
                    }
                }
            }
        }
        
        System.out.println("✅ Generated " + gradeCount + " grades for all students");
    }
    
    private void createGrade(Student student, Subject subject, Exam exam, Semester semester, 
                           Teacher teacher, Random random) {
        Grades grade = new Grades();
        
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setExam(exam);
        grade.setSemester(semester);
        grade.setExaminer(teacher);
        
        // Generate realistic grades - 80% pass, 20% fail
        boolean shouldPass = random.nextDouble() < 0.80;
        double score = generateScore(exam.getAssessmentType(), shouldPass, random);
        
        // Set scores based on assessment type
        if (exam.getAssessmentType() == AssessmentType.CC_1 || exam.getAssessmentType() == AssessmentType.CC_2) {
            grade.setCcScore(score);
            grade.setSnScore(0.0); // Initialize SN score
        } else {
            grade.setSnScore(score);
            grade.setCcScore(0.0); // Initialize CC score
        }
        
        // Calculate total score (CC + SN for complete subject grade)
        double totalScore = grade.getCcScore() + grade.getSnScore();
        grade.setTotalScore(totalScore);
        
        // Calculate GPA and pass/fail status
        double gpa = convertToGPA(totalScore);
        grade.setGpa(gpa);
        grade.setHasPassed(totalScore >= 50.0);
        
        grade.setComments(generateComment(score, exam.getAssessmentType()));
        grade.setCreatedDate(Instant.now());
        grade.setLastModifiedDate(Instant.now());
        
        gradeRepository.save(grade);
    }
    
    private String generateComment(double score, AssessmentType assessmentType) {
        double maxValue = getMaxValueForAssessment(assessmentType);
        double percentage = (score / maxValue) * 100;
        
        if (percentage >= 85) return "Excellent work!";
        if (percentage >= 70) return "Good performance";
        if (percentage >= 60) return "Satisfactory";
        if (percentage >= 50) return "Needs improvement";
        return "Requires significant improvement";
    }
    
    private double getMaxValueForAssessment(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> 30.0;
            case SN_1, SN_2 -> 70.0;
        };
    }

    private double generateScore(AssessmentType assessmentType, boolean shouldPass, Random random) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> {
                // CC: 0-30 points, pass >= 15 (50% of 30)
                if (shouldPass) {
                    // Generate scores between 15-30 with distribution favoring higher scores
                    double baseScore = 15 + random.nextDouble() * 15;
                    // Add some excellent performers (20% chance for 25-30)
                    if (random.nextDouble() < 0.2) {
                        baseScore = 25 + random.nextDouble() * 5;
                    }
                    yield Math.min(30.0, baseScore);
                } else {
                    yield random.nextDouble() * 15; // 0-15
                }
            }
            case SN_1, SN_2 -> {
                // SN: 0-70 points, pass >= 35 (50% of 70)
                if (shouldPass) {
                    // Generate scores between 35-70 with distribution favoring higher scores
                    double baseScore = 35 + random.nextDouble() * 35;
                    // Add some excellent performers (20% chance for 60-70)
                    if (random.nextDouble() < 0.2) {
                        baseScore = 60 + random.nextDouble() * 10;
                    }
                    yield Math.min(70.0, baseScore);
                } else {
                    yield random.nextDouble() * 35; // 0-35
                }
            }
        };
    }

    private double convertToGPA(double totalScore) {
        // Convert total score (0-100) to GPA (0-4.0)
        double percentage = totalScore;
        if (percentage >= 90) return 4.0;
        if (percentage >= 85) return 3.7;
        if (percentage >= 80) return 3.3;
        if (percentage >= 75) return 3.0;
        if (percentage >= 70) return 2.7;
        if (percentage >= 65) return 2.3;
        if (percentage >= 60) return 2.0;
        if (percentage >= 55) return 1.7;
        if (percentage >= 50) return 1.3;
        if (percentage >= 45) return 1.0;
        return 0.0;
    }

    private void generateTranscripts() {
        System.out.println("📜 Generating transcripts...");
        
        List<Student> students = studentRepository.findAll();
        Semester activeSemester = semesterRepository.findByActiveTrue().orElse(semesterRepository.findAll().get(0));
        Random random = new Random();
        
        int transcriptCount = 0;
        int passedCount = 0;
        
        for (Student student : students) {
            // Check if transcript already exists
            if (transcriptRepository.existsByStudent(student)) {
                continue;
            }
            
            List<Grades> studentGrades = gradeRepository.findByStudent(student);
            
            if (!studentGrades.isEmpty()) {
                Transcript transcript = new Transcript();
                transcript.setStudent(student);
                transcript.setSemester(activeSemester);
                transcript.setCreatedDate(Instant.now());
                transcript.setLastModifiedDate(Instant.now());
                
                // Calculate proper GPA by combining CC and SN scores per subject
                double avgGpa = calculateStudentGPA(studentGrades);
                
                // Ensure 75% pass rate - adjust GPA if needed
                boolean shouldPass = (passedCount < (transcriptCount + 1) * 0.75) || random.nextDouble() < 0.75;
                if (shouldPass && avgGpa < 2.0) {
                    avgGpa = 2.0 + random.nextDouble() * 2.0; // 2.0-4.0 range
                } else if (!shouldPass && avgGpa >= 2.0) {
                    avgGpa = random.nextDouble() * 2.0; // 0.0-2.0 range
                }
                
                transcript.setGpa(avgGpa);
                transcript.setStatus(avgGpa >= 2.0 ? TranscriptStatus.PASSED : TranscriptStatus.FAILED);
                
                if (avgGpa >= 2.0) {
                    passedCount++;
                }
                
                transcriptRepository.save(transcript);
                transcriptCount++;
                System.out.println("✅ Created transcript for " + student.getUsername() + " (GPA: " + String.format("%.2f", avgGpa) + ", Status: " + transcript.getStatus() + ")");
            } else {
                System.out.println("⚠️ No grades found for " + student.getUsername());
            }
        }
        
        double actualPassRate = transcriptCount > 0 ? (double) passedCount / transcriptCount * 100 : 0;
        System.out.println("✅ Generated " + transcriptCount + " transcripts (" + passedCount + " passed, " + String.format("%.1f", actualPassRate) + "% pass rate)");
    }
    
    private double calculateStudentGPA(List<Grades> studentGrades) {
        // Group grades by subject to combine CC and SN scores
        Map<Subject, List<Grades>> gradesBySubject = studentGrades.stream()
            .collect(java.util.stream.Collectors.groupingBy(Grades::getSubject));
        
        List<Double> subjectGPAs = new ArrayList<>();
        
        for (Map.Entry<Subject, List<Grades>> entry : gradesBySubject.entrySet()) {
            List<Grades> subjectGrades = entry.getValue();
            
            double ccTotal = subjectGrades.stream()
                .mapToDouble(g -> g.getCcScore() != null ? g.getCcScore() : 0.0)
                .sum();
            
            double snTotal = subjectGrades.stream()
                .mapToDouble(g -> g.getSnScore() != null ? g.getSnScore() : 0.0)
                .sum();
            
            double totalScore = ccTotal + snTotal;
            double subjectGPA = convertToGPA(totalScore);
            subjectGPAs.add(subjectGPA);
        }
        
        return subjectGPAs.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    private String getColorForAssessmentType(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1 -> "#4CAF50"; // Green
            case CC_2 -> "#2196F3"; // Blue
            case SN_1 -> "#FF9800"; // Orange
            case SN_2 -> "#F44336"; // Red
        };
    }
}