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

@Component
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
        // Semester 1 - Total: 30 credits
        createSubject("Programming Fundamentals", "CS101", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Introduction to programming concepts and problem-solving");
        createSubject("Mathematics for CS", "MATH101", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Mathematical foundations for computer science");
        createSubject("Computer Systems", "CS102", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Introduction to computer hardware and systems");
        createSubject("Discrete Mathematics", "MATH102", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Logic, sets, and discrete structures");
        createSubject("English Communication", "ENG101", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Academic writing and communication skills");
        createSubject("Physics I", "PHY101", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Mechanics and thermodynamics");

        // Semester 2 - Total: 30 credits
        createSubject("Data Structures", "CS201", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Arrays, linked lists, trees, and graphs");
        createSubject("Object-Oriented Programming", "CS202", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "OOP principles and design patterns");
        createSubject("Linear Algebra", "MATH201", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Vectors, matrices, and linear transformations");
        createSubject("Database Systems", "CS203", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Database design and SQL");
        createSubject("Statistics", "STAT201", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Probability and statistical analysis");
        createSubject("Web Development", "CS204", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "HTML, CSS, JavaScript fundamentals");
    }

    private void createMathSubjects(Department dept, Semester sem1, Semester sem2) {
        // Semester 1 - Total: 30 credits
        createSubject("Calculus I", "MATH111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR, "Limits, derivatives, and applications");
        createSubject("Linear Algebra I", "MATH112", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Vector spaces and linear transformations");
        createSubject("Mathematical Logic", "MATH113", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Propositional and predicate logic");
        createSubject("Introduction to Proofs", "MATH114", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Mathematical reasoning and proof techniques");
        createSubject("Computer Programming", "CS111", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Programming for mathematicians");
        createSubject("Academic Writing", "ENG111", new BigDecimal("2"), dept, sem1, StudentCycle.BACHELOR, "Mathematical writing and communication");

        // Semester 2 - Total: 30 credits
        createSubject("Calculus II", "MATH211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR, "Integration and series");
        createSubject("Abstract Algebra", "MATH212", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Groups, rings, and fields");
        createSubject("Differential Equations", "MATH213", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Ordinary differential equations");
        createSubject("Probability Theory", "STAT211", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Probability distributions and theory");
        createSubject("Numerical Analysis", "MATH214", new BigDecimal("3"), dept, sem2, StudentCycle.BACHELOR, "Computational mathematics");
        createSubject("Mathematical Software", "MATH215", new BigDecimal("2"), dept, sem2, StudentCycle.BACHELOR, "MATLAB, Mathematica, and R");
    }

    private void createPhysicsSubjects(Department dept, Semester sem1, Semester sem2) {
        // Semester 1 - Total: 30 credits
        createSubject("Classical Mechanics", "PHY111", new BigDecimal("8"), dept, sem1, StudentCycle.BACHELOR, "Newton's laws and mechanical systems");
        createSubject("Calculus for Physics", "MATH121", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Mathematical methods in physics");
        createSubject("Laboratory Physics I", "PHY112", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Experimental techniques and measurements");
        createSubject("Introduction to Modern Physics", "PHY113", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Quantum mechanics and relativity");
        createSubject("Vector Analysis", "MATH122", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Vector calculus for physics");
        createSubject("Scientific Computing", "CS121", new BigDecimal("3"), dept, sem1, StudentCycle.BACHELOR, "Programming for scientific applications");

        // Semester 2 - Total: 30 credits
        createSubject("Electromagnetism", "PHY211", new BigDecimal("8"), dept, sem2, StudentCycle.BACHELOR, "Electric and magnetic fields");
        createSubject("Physics Thermodynamics", "PHY212", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Heat, work, and statistical mechanics");
        createSubject("Laboratory Physics II", "PHY213", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Advanced experimental methods");
        createSubject("Waves and Optics", "PHY214", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Wave phenomena and optical systems");
        createSubject("Mathematical Physics", "MATH221", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Differential equations in physics");
        createSubject("Electronics", "PHY215", new BigDecimal("3"), dept, sem2, StudentCycle.BACHELOR, "Circuit analysis and electronic devices");
    }

    private void createBusinessSubjects(Department dept, Semester sem1, Semester sem2) {
        // Semester 1 - Total: 30 credits
        createSubject("Principles of Management", "BUS111", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Management theories and practices");
        createSubject("Financial Accounting", "ACC111", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Basic accounting principles");
        createSubject("Business Mathematics", "MATH131", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Mathematical applications in business");
        createSubject("Microeconomics", "ECO111", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Individual and firm behavior");
        createSubject("Business Communication", "ENG131", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Professional writing and presentation");
        createSubject("Introduction to Business", "BUS112", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Business fundamentals and ethics");

        // Semester 2 - Total: 30 credits
        createSubject("Marketing Principles", "MKT211", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Marketing strategies and consumer behavior");
        createSubject("Managerial Accounting", "ACC211", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Cost accounting and budgeting");
        createSubject("Macroeconomics", "ECO211", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "National and international economics");
        createSubject("Business Statistics", "STAT231", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Statistical analysis for business");
        createSubject("Organizational Behavior", "BUS211", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Human behavior in organizations");
        createSubject("Business Law", "LAW211", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Legal environment of business");
    }

    private void createEngineeringSubjects(Department dept, Semester sem1, Semester sem2) {
        // Semester 1 - Total: 30 credits
        createSubject("Engineering Mathematics I", "MATH141", new BigDecimal("7"), dept, sem1, StudentCycle.BACHELOR, "Calculus and differential equations");
        createSubject("Engineering Physics", "PHY141", new BigDecimal("6"), dept, sem1, StudentCycle.BACHELOR, "Physics principles for engineers");
        createSubject("Engineering Drawing", "ENG141", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Technical drawing and CAD");
        createSubject("Materials Science", "ENG142", new BigDecimal("5"), dept, sem1, StudentCycle.BACHELOR, "Properties of engineering materials");
        createSubject("Programming for Engineers", "CS141", new BigDecimal("4"), dept, sem1, StudentCycle.BACHELOR, "Programming and computational methods");
        createSubject("Engineering Ethics", "ENG143", new BigDecimal("2"), dept, sem1, StudentCycle.BACHELOR, "Professional ethics and responsibility");
        createSubject("Workshop Practice", "ENG144", new BigDecimal("2"), dept, sem1, StudentCycle.BACHELOR, "Hands-on manufacturing techniques");

        // Semester 2 - Total: 30 credits
        createSubject("Engineering Mathematics II", "MATH241", new BigDecimal("7"), dept, sem2, StudentCycle.BACHELOR, "Linear algebra and complex analysis");
        createSubject("Mechanics of Materials", "ENG241", new BigDecimal("6"), dept, sem2, StudentCycle.BACHELOR, "Stress, strain, and material behavior");
        createSubject("Engineering Thermodynamics", "ENG242", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Energy systems and heat transfer");
        createSubject("Electrical Circuits", "EEE241", new BigDecimal("5"), dept, sem2, StudentCycle.BACHELOR, "Circuit analysis and design");
        createSubject("Engineering Statistics", "STAT241", new BigDecimal("4"), dept, sem2, StudentCycle.BACHELOR, "Statistical methods in engineering");
        createSubject("Engineering Design", "ENG243", new BigDecimal("3"), dept, sem2, StudentCycle.BACHELOR, "Design process and methodology");
    }

    private void createSubject(String name, String code, BigDecimal credits, Department dept, 
                              Semester semester, StudentCycle cycle, String description) {
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
    }
    
    private List<TeachingLevel> determineSubjectLevels(String code) {
        List<TeachingLevel> levels = new ArrayList<>();
        // This is a simplified approach - in reality you'd have proper level assignment
        // For now, we'll leave it empty and let the teacher assignment handle it
        return levels;
    }
}