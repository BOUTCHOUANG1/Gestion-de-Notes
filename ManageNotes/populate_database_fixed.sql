-- University Database Population Script (Fixed)
-- Based on actual codebase logic: 2 semesters (S1, S2) and 4 grading windows (CC_1, SN_1, CC_2, SN_2)

-- Clear existing data
DELETE FROM grade_claims;
DELETE FROM grades;
DELETE FROM user_levels;
DELETE FROM subject;
DELETE FROM grading_window;
DELETE FROM semesters;
DELETE FROM students;
DELETE FROM users;
DELETE FROM departments;

-- 1. Insert Departments
INSERT INTO departments (id, name, creation_date, last_modified_date) VALUES
(1, 'Computer Science', NOW(), NOW()),
(2, 'Mathematics', NOW(), NOW()),
(3, 'Physics', NOW(), NOW()),
(4, 'Engineering', NOW(), NOW()),
(5, 'Business Administration', NOW(), NOW());

-- 2. Insert Admin Users
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, phone, department, creation_date, last_modified_date) VALUES
(1, 'admin', 'System', 'Administrator', 'admin@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN', true, false, '+237123456789', 'Administration', NOW(), NOW());

-- 3. Insert Teachers
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, phone, department, creation_date, last_modified_date) VALUES
(2, 'prof.johnson', 'Michael', 'Johnson', 'mjohnson@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+237123456791', 'Computer Science', NOW(), NOW()),
(3, 'prof.williams', 'Sarah', 'Williams', 'swilliams@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+237123456792', 'Mathematics', NOW(), NOW()),
(4, 'prof.brown', 'David', 'Brown', 'dbrown@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+237123456793', 'Physics', NOW(), NOW()),
(5, 'prof.davis', 'Emily', 'Davis', 'edavis@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+237123456794', 'Engineering', NOW(), NOW());

-- 4. Insert Teacher Levels
INSERT INTO user_levels (user_id, level) VALUES
(2, 'LEVEL1'), (2, 'LEVEL2'), (2, 'LEVEL3'),
(3, 'LEVEL1'), (3, 'LEVEL2'), (3, 'LEVEL3'),
(4, 'LEVEL2'), (4, 'LEVEL3'), (4, 'LEVEL4'),
(5, 'LEVEL3'), (5, 'LEVEL4'), (5, 'LEVEL5');

-- 5. Insert Students
INSERT INTO students (id, first_name, last_name, matricule, level, email, speciality, cycle, date_of_birth, place_of_birth, creation_date, last_modified_date) VALUES
-- LEVEL1 Students
(1, 'Alice', 'Cooper', 'STU2024001', 'LEVEL1', 'alice.cooper@student.university.edu', 'Computer Science', 'BACHELOR', '2005-03-15', 'Yaoundé', NOW(), NOW()),
(2, 'Bob', 'Martin', 'STU2024002', 'LEVEL1', 'bob.martin@student.university.edu', 'Mathematics', 'BACHELOR', '2005-07-22', 'Douala', NOW(), NOW()),
(3, 'Carol', 'White', 'STU2024003', 'LEVEL1', 'carol.white@student.university.edu', 'Physics', 'BACHELOR', '2005-01-10', 'Bamenda', NOW(), NOW()),
-- LEVEL2 Students
(4, 'Daniel', 'Green', 'STU2023001', 'LEVEL2', 'daniel.green@student.university.edu', 'Computer Science', 'BACHELOR', '2004-04-12', 'Yaoundé', NOW(), NOW()),
(5, 'Eva', 'Black', 'STU2023002', 'LEVEL2', 'eva.black@student.university.edu', 'Mathematics', 'BACHELOR', '2004-08-30', 'Douala', NOW(), NOW()),
(6, 'Frank', 'Blue', 'STU2023003', 'LEVEL2', 'frank.blue@student.university.edu', 'Physics', 'BACHELOR', '2004-02-14', 'Bamenda', NOW(), NOW()),
-- LEVEL3 Students
(7, 'Grace', 'Red', 'STU2022001', 'LEVEL3', 'grace.red@student.university.edu', 'Computer Science', 'BACHELOR', '2003-05-20', 'Yaoundé', NOW(), NOW()),
(8, 'Henry', 'Yellow', 'STU2022002', 'LEVEL3', 'henry.yellow@student.university.edu', 'Engineering', 'BACHELOR', '2003-09-15', 'Douala', NOW(), NOW()),
-- LEVEL4 Students (Master)
(9, 'Iris', 'Purple', 'STU2021001', 'LEVEL4', 'iris.purple@student.university.edu', 'Computer Science', 'MASTER', '2002-01-15', 'Yaoundé', NOW(), NOW()),
(10, 'Jack', 'Orange', 'STU2021002', 'LEVEL4', 'jack.orange@student.university.edu', 'Engineering', 'MASTER', '2002-05-22', 'Douala', NOW(), NOW());

-- 6. Insert Semesters (Only S1 and S2 as per codebase)
INSERT INTO semesters (id, name, start_date, end_date, active, order_index, creation_date, last_modified_date) VALUES
(1, 'S1 2024', '2024-01-01', '2024-06-30', true, 1, NOW(), NOW()),
(2, 'S2 2024', '2024-07-01', '2024-12-31', false, 2, NOW(), NOW());

-- 7. Insert Grading Windows (4 windows: CC_1, SN_1, CC_2, SN_2)
INSERT INTO grading_window (id, name, short_name, type, id_semester, start_date, end_date, color, is_active, order_index, period_type, creation_date, last_modified_date) VALUES
-- S1 2024 Windows
(1, 'Continuous Assessment 1', 'CC_1', 'CC', 1, '2024-02-01', '2024-02-15', '#FF6B6B', true, 1, 0, NOW(), NOW()),
(2, 'Session Normale 1', 'SN_1', 'SN', 1, '2024-05-15', '2024-06-15', '#45B7D1', false, 2, 2, NOW(), NOW()),
-- S2 2024 Windows  
(3, 'Continuous Assessment 2', 'CC_2', 'CC', 2, '2024-08-01', '2024-08-15', '#4ECDC4', false, 3, 1, NOW(), NOW()),
(4, 'Session Normale 2', 'SN_2', 'SN', 2, '2024-11-15', '2024-12-15', '#96CEB4', false, 4, 3, NOW(), NOW());

-- 8. Insert Subjects for S1 2024
INSERT INTO subject (id, name, code, credits, id_teacher, description, level, cycle, id_semester, department_id, active, creation_date, last_modified_date) VALUES
-- LEVEL1 Subjects for S1
(1, 'Introduction to Programming', 'CS101', 4.0, 2, 'Basic programming concepts using Python', 'LEVEL1', 'BACHELOR', 1, 1, true, NOW(), NOW()),
(2, 'Calculus I', 'MATH101', 3.0, 3, 'Differential and integral calculus', 'LEVEL1', 'BACHELOR', 1, 2, true, NOW(), NOW()),
(3, 'Physics I', 'PHYS101', 3.0, 4, 'Mechanics and thermodynamics', 'LEVEL1', 'BACHELOR', 1, 3, true, NOW(), NOW()),
-- LEVEL2 Subjects for S1
(4, 'Data Structures', 'CS201', 4.0, 2, 'Arrays, linked lists, trees, and graphs', 'LEVEL2', 'BACHELOR', 1, 1, true, NOW(), NOW()),
(5, 'Linear Algebra', 'MATH201', 3.0, 3, 'Vectors, matrices, and linear transformations', 'LEVEL2', 'BACHELOR', 1, 2, true, NOW(), NOW()),
(6, 'Electricity and Magnetism', 'PHYS201', 3.0, 4, 'Electric fields, magnetic fields, and circuits', 'LEVEL2', 'BACHELOR', 1, 3, true, NOW(), NOW()),
-- LEVEL3 Subjects for S1
(7, 'Algorithms', 'CS301', 4.0, 2, 'Algorithm design and analysis', 'LEVEL3', 'BACHELOR', 1, 1, true, NOW(), NOW()),
(8, 'Thermodynamics', 'ENG301', 3.0, 5, 'Heat transfer and energy systems', 'LEVEL3', 'BACHELOR', 1, 4, true, NOW(), NOW()),
-- LEVEL4 Subjects for S1
(9, 'Machine Learning', 'CS401', 4.0, 2, 'Supervised and unsupervised learning algorithms', 'LEVEL4', 'MASTER', 1, 1, true, NOW(), NOW()),
(10, 'Advanced Mechanics', 'ENG401', 3.0, 5, 'Advanced topics in mechanical engineering', 'LEVEL4', 'MASTER', 1, 4, true, NOW(), NOW());

-- 9. Insert Grades for S1 2024 (CC_1 completed)
INSERT INTO grades (id, value, max_value, comments, period_label, id_students, id_subject, id_users, id_semester, grade_type, creation_date, last_modified_date) VALUES
-- Alice Cooper (LEVEL1) - CC_1 grades
(1, 15.5, 20.0, 'Good understanding of programming basics', 'CC_1', 1, 1, 2, 1, 'CC', NOW(), NOW()),
(2, 14.0, 20.0, 'Needs improvement in calculus', 'CC_1', 1, 2, 3, 1, 'CC', NOW(), NOW()),
(3, 16.0, 20.0, 'Excellent physics understanding', 'CC_1', 1, 3, 4, 1, 'CC', NOW(), NOW()),

-- Bob Martin (LEVEL1) - CC_1 grades
(4, 17.0, 20.0, 'Excellent programming skills', 'CC_1', 2, 1, 2, 1, 'CC', NOW(), NOW()),
(5, 18.0, 20.0, 'Outstanding mathematical ability', 'CC_1', 2, 2, 3, 1, 'CC', NOW(), NOW()),
(6, 15.5, 20.0, 'Good physics performance', 'CC_1', 2, 3, 4, 1, 'CC', NOW(), NOW()),

-- Carol White (LEVEL1) - CC_1 grades
(7, 13.5, 20.0, 'Basic programming understanding', 'CC_1', 3, 1, 2, 1, 'CC', NOW(), NOW()),
(8, 15.0, 20.0, 'Good calculus progress', 'CC_1', 3, 2, 3, 1, 'CC', NOW(), NOW()),
(9, 17.5, 20.0, 'Excellent physics aptitude', 'CC_1', 3, 3, 4, 1, 'CC', NOW(), NOW()),

-- Daniel Green (LEVEL2) - CC_1 grades
(10, 16.5, 20.0, 'Good data structures knowledge', 'CC_1', 4, 4, 2, 1, 'CC', NOW(), NOW()),
(11, 15.0, 20.0, 'Linear algebra progress', 'CC_1', 4, 5, 3, 1, 'CC', NOW(), NOW()),
(12, 14.5, 20.0, 'Physics concepts understood', 'CC_1', 4, 6, 4, 1, 'CC', NOW(), NOW()),

-- Eva Black (LEVEL2) - CC_1 grades
(13, 17.5, 20.0, 'Excellent data structures work', 'CC_1', 5, 4, 2, 1, 'CC', NOW(), NOW()),
(14, 18.0, 20.0, 'Outstanding linear algebra', 'CC_1', 5, 5, 3, 1, 'CC', NOW(), NOW()),
(15, 16.0, 20.0, 'Good physics understanding', 'CC_1', 5, 6, 4, 1, 'CC', NOW(), NOW()),

-- Grace Red (LEVEL3) - CC_1 grades
(16, 18.5, 20.0, 'Exceptional algorithm skills', 'CC_1', 7, 7, 2, 1, 'CC', NOW(), NOW()),
(17, 16.5, 20.0, 'Good thermodynamics grasp', 'CC_1', 7, 8, 5, 1, 'CC', NOW(), NOW()),

-- Henry Yellow (LEVEL3) - CC_1 grades
(18, 15.0, 20.0, 'Algorithm concepts developing', 'CC_1', 8, 7, 2, 1, 'CC', NOW(), NOW()),
(19, 17.0, 20.0, 'Strong thermodynamics work', 'CC_1', 8, 8, 5, 1, 'CC', NOW(), NOW()),

-- Iris Purple (LEVEL4) - CC_1 grades
(20, 17.0, 20.0, 'Excellent ML understanding', 'CC_1', 9, 9, 2, 1, 'CC', NOW(), NOW()),
(21, 16.0, 20.0, 'Good advanced mechanics', 'CC_1', 9, 10, 5, 1, 'CC', NOW(), NOW()),

-- Jack Orange (LEVEL4) - CC_1 grades
(22, 15.5, 20.0, 'ML concepts progressing', 'CC_1', 10, 9, 2, 1, 'CC', NOW(), NOW()),
(23, 18.0, 20.0, 'Outstanding mechanics work', 'CC_1', 10, 10, 5, 1, 'CC', NOW(), NOW());

-- 10. Insert Grade Claims
INSERT INTO grade_claims (id, student_id, grade_id, semester_id, period_label, requested_score, cause, description, teacher_comment, status, creation_date, last_modified_date) VALUES
(1, 1, 2, 1, 'CC_1', 16.0, 'CALCULATION_ERROR', 'I believe there was an error in my calculus grade calculation.', 'After review, the grade is correct.', 'REJECTED', NOW(), NOW()),
(2, 3, 7, 1, 'CC_1', 15.0, 'MISSING_WORK', 'I submitted additional programming exercises that were not graded.', NULL, 'PENDING', NOW(), NOW());

-- Display summary
SELECT 'Database populated successfully with university structure!' as message;
SELECT 'Departments: ' || COUNT(*) as count FROM departments
UNION ALL
SELECT 'Users: ' || COUNT(*) FROM users
UNION ALL
SELECT 'Students: ' || COUNT(*) FROM students
UNION ALL
SELECT 'Semesters: ' || COUNT(*) FROM semesters
UNION ALL
SELECT 'Grading Windows: ' || COUNT(*) FROM grading_window
UNION ALL
SELECT 'Subjects: ' || COUNT(*) FROM subject
UNION ALL
SELECT 'Grades: ' || COUNT(*) FROM grades
UNION ALL
SELECT 'Grade Claims: ' || COUNT(*) FROM grade_claims;