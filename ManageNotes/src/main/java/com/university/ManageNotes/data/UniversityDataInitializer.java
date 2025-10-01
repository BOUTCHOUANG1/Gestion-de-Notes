package com.university.ManageNotes.data;

import com.university.ManageNotes.model.*;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.university.ManageNotes.model.enums.StudentLevel;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @Component
@RequiredArgsConstructor
@Order(2)
public class UniversityDataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final TeacherRepository teacherRepository;

    @Override
    public void run(String... args) throws Exception {
        long deptCount = departmentRepository.count();
        long subjectCount = subjectRepository.count();
        System.out.println("📊 Current data: Departments=" + deptCount + ", Subjects=" + subjectCount);
        
        if (deptCount == 0 && subjectCount == 0) {
            System.out.println("🚀 Starting university data initialization...");
            initializeSemesters();
            initializeDepartmentsAndSubjects();
            System.out.println("✅ University test data initialized successfully");
        } else {
            System.out.println("ℹ️ University test data already exists, skipping initialization");
        }
    }

    private void initializeSemesters() {
        if (semesterRepository.count() == 0) {
            // Semester 1: September to February
            Semester semester1 = new Semester();
            semester1.setName("Semester 1 - 2024/2025");
            semester1.setStartDate(LocalDate.of(2024, 9, 8));
            semester1.setEndDate(LocalDate.of(2025, 2, 23));
            semester1.setActive(true);
            semester1.setCreatedDate(Instant.now());
            semester1.setLastModifiedDate(Instant.now());
            semesterRepository.save(semester1);

            // Semester 2: March to June
            Semester semester2 = new Semester();
            semester2.setName("Semester 2 - 2024/2025");
            semester2.setStartDate(LocalDate.of(2025, 3, 15));
            semester2.setEndDate(LocalDate.of(2025, 6, 2));
            semester2.setActive(false);
            semester2.setCreatedDate(Instant.now());
            semester2.setLastModifiedDate(Instant.now());
            semesterRepository.save(semester2);
        }
    }

    private void initializeDepartmentsAndSubjects() {
        Semester semester1 = semesterRepository.findByName("Semester 1 - 2024/2025").orElse(null);
        Semester semester2 = semesterRepository.findByName("Semester 2 - 2024/2025").orElse(null);

        // Computer Science Department
        Department csDept = createDepartment("Computer Science");
        createCSSubjects(csDept, semester1, semester2);

        // Mathematics Department
        Department mathDept = createDepartment("Mathematics");
        createMathSubjects(mathDept, semester1, semester2);

        // Physics Department
        Department physicsDept = createDepartment("Physics");
        createPhysicsSubjects(physicsDept, semester1, semester2);

        // Business Administration Department
        Department businessDept = createDepartment("Business Administration");
        createBusinessSubjects(businessDept, semester1, semester2);

        // Engineering Department
        Department engineeringDept = createDepartment("Engineering");
        createEngineeringSubjects(engineeringDept, semester1, semester2);
    }

    private Department createDepartment(String name) {
        Department dept = new Department();
        dept.setDepartmentName(name);
        dept.setCreatedDate(Instant.now());
        dept.setLastModifiedDate(Instant.now());
        return departmentRepository.save(dept);
    }

    private void createCSSubjects(Department dept, Semester sem1, Semester sem2) {
        // Level 1 - Semester 1
        createSubject("Programming Fundamentals", "CS101", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Introduction to programming concepts and problem-solving");
        createSubject("Mathematics for CS", "MATH101", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Mathematical foundations for computer science");
        createSubject("Computer Systems", "CS102", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Introduction to computer hardware and systems");
        createSubject("Discrete Mathematics", "MATH102", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Logic, sets, and discrete structures");
        createSubject("English Communication", "ENG101", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Academic writing and communication skills");
        createSubject("Physics I", "PHY101", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Mechanics and thermodynamics");

        // Level 2 - Semester 2
        createSubject("Data Structures", "CS201", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Arrays, linked lists, trees, and graphs");
        createSubject("Object-Oriented Programming", "CS202", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "OOP principles and design patterns");
        createSubject("Linear Algebra", "MATH201", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Vectors, matrices, and linear transformations");
        createSubject("Database Systems", "CS203", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Database design and SQL");
        createSubject("Statistics", "STAT201", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Probability and statistical analysis");
        createSubject("Web Development", "CS204", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "HTML, CSS, JavaScript fundamentals");
        
        // Level 3 - Advanced Bachelor
        createSubject("Algorithms", "CS301", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Algorithm design and analysis");
        createSubject("Software Engineering", "CS302", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Software development methodologies");
        
        // Level 4 - Master's
        createSubject("Machine Learning", "CS401", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Introduction to ML algorithms");
        createSubject("Advanced Databases", "CS402", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Advanced database concepts");
        
        // Level 5 - Advanced Master's
        createSubject("AI Research", "CS501", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Artificial Intelligence research methods");
        createSubject("Thesis Project", "CS502", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Master's thesis project");
    }

    private void createMathSubjects(Department dept, Semester sem1, Semester sem2) {
        // Level 1
        createSubject("Calculus I", "MATH111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR, "Limits, derivatives, and applications");
        createSubject("Linear Algebra I", "MATH112", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Vector spaces and linear transformations");
        createSubject("Mathematical Logic", "MATH113", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Propositional and predicate logic");
        createSubject("Introduction to Proofs", "MATH114", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Mathematical reasoning and proof techniques");

        // Level 2
        createSubject("Calculus II", "MATH211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR, "Integration and series");
        createSubject("Abstract Algebra", "MATH212", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Groups, rings, and fields");
        createSubject("Differential Equations", "MATH213", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Ordinary differential equations");
        createSubject("Probability Theory", "STAT211", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Probability distributions and theory");
        
        // Level 3
        createSubject("Real Analysis", "MATH311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Advanced calculus and analysis");
        createSubject("Complex Analysis", "MATH312", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Functions of complex variables");
        
        // Level 4
        createSubject("Advanced Analysis", "MATH411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Measure theory and functional analysis");
        createSubject("Topology", "MATH412", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "General and algebraic topology");
        
        // Level 5
        createSubject("Mathematical Research Methods", "MATH511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Mathematical research methodology");
        createSubject("Mathematics Master's Thesis", "MATH512", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Independent research project");
    }

    private void createPhysicsSubjects(Department dept, Semester sem1, Semester sem2) {
        // Level 1
        createSubject("Classical Mechanics", "PHY111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR, "Newton's laws and mechanical systems");
        createSubject("Calculus for Physics", "MATH121", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Mathematical methods in physics");
        createSubject("Laboratory Physics I", "PHY112", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Experimental techniques and measurements");
        createSubject("Introduction to Modern Physics", "PHY113", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Quantum mechanics and relativity");

        // Level 2
        createSubject("Electromagnetism", "PHY211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR, "Electric and magnetic fields");
        createSubject("Physics Thermodynamics", "PHY212", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Heat, work, and statistical mechanics");
        createSubject("Laboratory Physics II", "PHY213", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Advanced experimental methods");
        createSubject("Waves and Optics", "PHY214", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Wave phenomena and optical systems");
        
        // Level 3
        createSubject("Quantum Mechanics", "PHY311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Advanced quantum theory");
        createSubject("Statistical Physics", "PHY312", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Statistical mechanics and thermodynamics");
        
        // Level 4
        createSubject("Advanced Quantum", "PHY411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Quantum field theory");
        createSubject("Solid State Physics", "PHY412", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Physics of condensed matter");
        
        // Level 5
        createSubject("Physics Research Project", "PHY511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Independent physics research");
        createSubject("Advanced Physics Topics", "PHY512", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Specialized physics topics");
    }

    private void createBusinessSubjects(Department dept, Semester sem1, Semester sem2) {
        // Level 1
        createSubject("Principles of Management", "BUS111", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Management theories and practices");
        createSubject("Financial Accounting", "ACC111", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Basic accounting principles");
        createSubject("Business Mathematics", "MATH131", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Mathematical applications in business");
        createSubject("Microeconomics", "ECO111", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Individual and firm behavior");

        // Level 2
        createSubject("Marketing Principles", "MKT211", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Marketing strategies and consumer behavior");
        createSubject("Managerial Accounting", "ACC211", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Cost accounting and budgeting");
        createSubject("Macroeconomics", "ECO211", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "National and international economics");
        createSubject("Business Statistics", "STAT231", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Statistical analysis for business");
        
        // Level 3
        createSubject("Strategic Management", "BUS311", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Corporate strategy and planning");
        createSubject("International Business", "BUS312", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Global business operations");
        
        // Level 4
        createSubject("Advanced Finance", "FIN411", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Corporate finance and investments");
        createSubject("Operations Research", "BUS411", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Quantitative business methods");
        
        // Level 5
        createSubject("Business Research Methods", "BUS511", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Research methodology in business");
        createSubject("MBA Capstone Project", "BUS512", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Integrated business project");
    }

    private void createEngineeringSubjects(Department dept, Semester sem1, Semester sem2) {
        // Level 1
        createSubject("Engineering Mathematics I", "MATH141", new BigDecimal("7"), dept, sem1, StudentCycle.BACHELOR, "Calculus and differential equations");
        createSubject("Engineering Physics", "PHY141", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Physics principles for engineers");
        createSubject("Engineering Drawing", "ENG141", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Technical drawing and CAD");
        createSubject("Materials Science", "ENG142", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Properties of engineering materials");

        // Level 2
        createSubject("Engineering Mathematics II", "MATH241", new BigDecimal("7"), dept, sem2, StudentCycle.BACHELOR, "Linear algebra and complex analysis");
        createSubject("Mechanics of Materials", "ENG241", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Stress, strain, and material behavior");
        createSubject("Engineering Thermodynamics", "ENG242", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Energy systems and heat transfer");
        createSubject("Electrical Circuits", "EEE241", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Circuit analysis and design");
        
        // Level 3
        createSubject("Control Systems", "ENG341", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Automatic control theory");
        createSubject("Fluid Mechanics", "ENG342", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Fluid statics and dynamics");
        
        // Level 4
        createSubject("Advanced Engineering", "ENG441", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Advanced engineering principles");
        createSubject("Project Management", "ENG442", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Engineering project management");
        
        // Level 5
        createSubject("Engineering Research Methods", "ENG541", new BigDecimal("6"), dept, sem1, StudentCycle.MASTER, "Engineering research methodology");
        createSubject("Engineering Master's Project", "ENG542", new BigDecimal("6"), dept, sem2, StudentCycle.MASTER, "Capstone engineering project");
    }

    private void createSubject(String name, String code, BigDecimal credits, Department dept, 
                              Semester semester, StudentCycle cycle, String description) {
        // Check if subject already exists
        if (subjectRepository.findAll().stream().anyMatch(s -> s.getSubjectName().equals(name))) {
            System.out.println("ℹ️ Subject already exists: " + name);
            return;
        }
        
        Subject subject = new Subject();
        subject.setSubjectName(name);
        subject.setSubjectCode(code);
        subject.setCredits(credits);
        subject.setDepartment(dept);
        subject.setSemester(semester);
        subject.setStudentcycle(cycle);
        subject.setDescription(description);
        
        // Subject levels will be set during teacher assignment
        // subject.setSubjectLevel(null);
        
        subjectRepository.save(subject);
        System.out.println("✅ Created subject: " + name);
    }
    
    private List<TeachingLevel> determineSubjectLevels(String code) {
        List<TeachingLevel> levels = new ArrayList<>();
        // This is a simplified approach - in reality you'd have proper level assignment
        // For now, we'll leave it empty and let the teacher assignment handle it
        return levels;
    }
}