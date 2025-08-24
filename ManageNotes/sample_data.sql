-- Sample Data for ManageNotes Database
-- Run this script to populate the database with test data

-- 1. Insert Departments
INSERT INTO departments (id, name, creation_date, last_modified_date) VALUES
(1, 'Computer Science', NOW(), NOW()),
(2, 'Mathematics', NOW(), NOW()),
(3, 'Physics', NOW(), NOW()),
(4, 'Chemistry', NOW(), NOW()),
(5, 'Biology', NOW(), NOW()),
(6, 'Engineering', NOW(), NOW()),
(7, 'Business Administration', NOW(), NOW()),
(8, 'Literature', NOW(), NOW()),
(9, 'History', NOW(), NOW()),
(10, 'Psychology', NOW(), NOW());

-- 2. Insert Semesters
INSERT INTO semesters (id, name, start_date, end_date, creation_date, last_modified_date) VALUES
(1, 'Fall 2024', '2024-09-01', '2024-12-31', NOW(), NOW()),
(2, 'Spring 2025', '2025-01-15', '2025-05-31', NOW(), NOW()),
(3, 'Summer 2025', '2025-06-01', '2025-08-31', NOW(), NOW()),
(4, 'Fall 2025', '2025-09-01', '2025-12-31', NOW(), NOW()),
(5, 'Spring 2026', '2026-01-15', '2026-05-31', NOW(), NOW()),
(6, 'Summer 2026', '2026-06-01', '2026-08-31', NOW(), NOW()),
(7, 'Fall 2026', '2026-09-01', '2026-12-31', NOW(), NOW()),
(8, 'Spring 2027', '2027-01-15', '2027-05-31', NOW(), NOW()),
(9, 'Summer 2027', '2027-06-01', '2027-08-31', NOW(), NOW()),
(10, 'Fall 2027', '2027-09-01', '2027-12-31', NOW(), NOW());

-- 3. Insert Users (Teachers and Admins)
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, phone, department, creation_date, last_modified_date) VALUES
(1, 'admin', 'System', 'Administrator', 'admin@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN', true, false, '+1234567890', 'Administration', NOW(), NOW()),
(2, 'john.doe', 'John', 'Doe', 'john.doe@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567891', 'Computer Science', NOW(), NOW()),
(3, 'jane.smith', 'Jane', 'Smith', 'jane.smith@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567892', 'Mathematics', NOW(), NOW()),
(4, 'mike.wilson', 'Mike', 'Wilson', 'mike.wilson@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567893', 'Physics', NOW(), NOW()),
(5, 'sarah.brown', 'Sarah', 'Brown', 'sarah.brown@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567894', 'Chemistry', NOW(), NOW()),
(6, 'david.jones', 'David', 'Jones', 'david.jones@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567895', 'Biology', NOW(), NOW()),
(7, 'lisa.garcia', 'Lisa', 'Garcia', 'lisa.garcia@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567896', 'Engineering', NOW(), NOW()),
(8, 'robert.miller', 'Robert', 'Miller', 'robert.miller@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567897', 'Business Administration', NOW(), NOW()),
(9, 'emily.davis', 'Emily', 'Davis', 'emily.davis@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567898', 'Literature', NOW(), NOW()),
(10, 'james.taylor', 'James', 'Taylor', 'james.taylor@university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'TEACHER', true, false, '+1234567899', 'History', NOW(), NOW());

-- 4. Insert User Levels (Teacher teaching levels)
INSERT INTO user_levels (user_id, level) VALUES
(2, 'LEVEL1'), (2, 'LEVEL2'),
(3, 'LEVEL1'), (3, 'LEVEL2'), (3, 'LEVEL3'),
(4, 'LEVEL2'), (4, 'LEVEL3'),
(5, 'LEVEL1'), (5, 'LEVEL2'),
(6, 'LEVEL1'), (6, 'LEVEL3'),
(7, 'LEVEL2'), (7, 'LEVEL3'), (7, 'LEVEL4'),
(8, 'LEVEL1'), (8, 'LEVEL2'), (8, 'LEVEL3'),
(9, 'LEVEL1'), (9, 'LEVEL2'),
(10, 'LEVEL1'), (10, 'LEVEL2'), (10, 'LEVEL3');

-- 5. Insert Students
INSERT INTO students (id, first_name, last_name, matricule, level, email, speciality, cycle, date_of_birth, place_of_birth, creation_date, last_modified_date) VALUES
(1, 'Alice', 'Johnson', 'STU2024001', 'LEVEL1', 'alice.johnson@student.university.com', 'Computer Science', 'BACHELOR', '2003-05-15', 'New York', NOW(), NOW()),
(2, 'Bob', 'Williams', 'STU2024002', 'LEVEL1', 'bob.williams@student.university.com', 'Mathematics', 'BACHELOR', '2003-08-22', 'Los Angeles', NOW(), NOW()),
(3, 'Carol', 'Martinez', 'STU2024003', 'LEVEL2', 'carol.martinez@student.university.com', 'Physics', 'BACHELOR', '2002-12-10', 'Chicago', NOW(), NOW()),
(4, 'Daniel', 'Anderson', 'STU2024004', 'LEVEL2', 'daniel.anderson@student.university.com', 'Chemistry', 'BACHELOR', '2002-03-18', 'Houston', NOW(), NOW()),
(5, 'Eva', 'Thompson', 'STU2024005', 'LEVEL3', 'eva.thompson@student.university.com', 'Biology', 'BACHELOR', '2001-11-05', 'Phoenix', NOW(), NOW()),
(6, 'Frank', 'White', 'STU2024006', 'LEVEL3', 'frank.white@student.university.com', 'Engineering', 'BACHELOR', '2001-07-30', 'Philadelphia', NOW(), NOW()),
(7, 'Grace', 'Harris', 'STU2024007', 'LEVEL4', 'grace.harris@student.university.com', 'Business Administration', 'BACHELOR', '2000-09-12', 'San Antonio', NOW(), NOW()),
(8, 'Henry', 'Clark', 'STU2024008', 'LEVEL1', 'henry.clark@student.university.com', 'Literature', 'BACHELOR', '2003-01-25', 'San Diego', NOW(), NOW()),
(9, 'Ivy', 'Lewis', 'STU2024009', 'LEVEL2', 'ivy.lewis@student.university.com', 'History', 'BACHELOR', '2002-06-08', 'Dallas', NOW(), NOW()),
(10, 'Jack', 'Walker', 'STU2024010', 'LEVEL1', 'jack.walker@student.university.com', 'Psychology', 'MASTER', '2001-04-14', 'San Jose', NOW(), NOW());

-- 6. Insert Student Users (for login)
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, creation_date, last_modified_date) VALUES
(11, 'STU2024001', 'Alice', 'Johnson', 'alice.johnson@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(12, 'STU2024002', 'Bob', 'Williams', 'bob.williams@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(13, 'STU2024003', 'Carol', 'Martinez', 'carol.martinez@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(14, 'STU2024004', 'Daniel', 'Anderson', 'daniel.anderson@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(15, 'STU2024005', 'Eva', 'Thompson', 'eva.thompson@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(16, 'STU2024006', 'Frank', 'White', 'frank.white@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(17, 'STU2024007', 'Grace', 'Harris', 'grace.harris@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(18, 'STU2024008', 'Henry', 'Clark', 'henry.clark@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(19, 'STU2024009', 'Ivy', 'Lewis', 'ivy.lewis@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(20, 'STU2024010', 'Jack', 'Walker', 'jack.walker@student.university.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW());

-- 7. Insert Subjects
INSERT INTO subject (id, name, code, credits, description, level, cycle, id_teacher, id_semester, department_id, active, creation_date, last_modified_date) VALUES
(1, 'Introduction to Programming', 'CS101', 3, 'Basic programming concepts and algorithms', 'LEVEL1', 'BACHELOR', 2, 1, 1, true, NOW(), NOW()),
(2, 'Data Structures', 'CS201', 4, 'Advanced data structures and their applications', 'LEVEL2', 'BACHELOR', 2, 1, 1, true, NOW(), NOW()),
(3, 'Calculus I', 'MATH101', 4, 'Differential and integral calculus', 'LEVEL1', 'BACHELOR', 3, 1, 2, true, NOW(), NOW()),
(4, 'Linear Algebra', 'MATH201', 3, 'Vector spaces and linear transformations', 'LEVEL2', 'BACHELOR', 3, 1, 2, true, NOW(), NOW()),
(5, 'Physics I', 'PHYS101', 4, 'Mechanics and thermodynamics', 'LEVEL1', 'BACHELOR', 4, 1, 3, true, NOW(), NOW()),
(6, 'Quantum Physics', 'PHYS301', 4, 'Introduction to quantum mechanics', 'LEVEL3', 'BACHELOR', 4, 1, 3, true, NOW(), NOW()),
(7, 'General Chemistry', 'CHEM101', 3, 'Basic chemical principles', 'LEVEL1', 'BACHELOR', 5, 1, 4, true, NOW(), NOW()),
(8, 'Organic Chemistry', 'CHEM201', 4, 'Structure and reactions of organic compounds', 'LEVEL2', 'BACHELOR', 5, 1, 4, true, NOW(), NOW()),
(9, 'Cell Biology', 'BIO101', 3, 'Structure and function of cells', 'LEVEL1', 'BACHELOR', 6, 1, 5, true, NOW(), NOW()),
(10, 'Genetics', 'BIO301', 4, 'Principles of heredity and gene expression', 'LEVEL3', 'BACHELOR', 6, 1, 5, true, NOW(), NOW());

-- 8. Insert Grading Windows
INSERT INTO grading_window (id, name, short_name, period_type, color, order_index, start_date, end_date, active, creation_date, last_modified_date) VALUES
(1, 'Continuous Assessment 1', 'CC1', 'CC1', '#FF6B6B', 1, '2024-09-15', '2024-10-15', true, NOW(), NOW()),
(2, 'Session Normale 1', 'SN1', 'SN1', '#4ECDC4', 2, '2024-10-20', '2024-11-10', true, NOW(), NOW()),
(3, 'Continuous Assessment 2', 'CC2', 'CC2', '#45B7D1', 3, '2024-11-15', '2024-12-15', true, NOW(), NOW()),
(4, 'Session Normale 2', 'SN2', 'SN2', '#96CEB4', 4, '2024-12-20', '2025-01-10', true, NOW(), NOW()),
(5, 'Rattrapage CC1', 'RCC1', 'CC1', '#FFEAA7', 5, '2025-01-15', '2025-02-05', true, NOW(), NOW()),
(6, 'Rattrapage SN1', 'RSN1', 'SN1', '#DDA0DD', 6, '2025-02-10', '2025-02-28', true, NOW(), NOW()),
(7, 'Rattrapage CC2', 'RCC2', 'CC2', '#FFB6C1', 7, '2025-03-05', '2025-03-25', true, NOW(), NOW()),
(8, 'Rattrapage SN2', 'RSN2', 'SN2', '#F0E68C', 8, '2025-03-30', '2025-04-15', true, NOW(), NOW()),
(9, 'Special Session', 'SPEC', 'CC1', '#D2691E', 9, '2025-04-20', '2025-05-10', true, NOW(), NOW()),
(10, 'Final Makeup', 'FINAL', 'SN2', '#CD853F', 10, '2025-05-15', '2025-05-30', true, NOW(), NOW());

-- 9. Insert Grades
INSERT INTO grades (id, value, max_value, comments, period_label, id_students, id_subject, id_users, id_semester, grade_type, creation_date, last_modified_date) VALUES
(1, 16.5, 20.0, 'Excellent work on programming fundamentals', 'CC1', 1, 1, 2, 1, 'ASSIGNMENT', NOW(), NOW()),
(2, 14.0, 20.0, 'Good understanding of data structures', 'CC1', 1, 2, 2, 1, 'EXAM', NOW(), NOW()),
(3, 18.0, 20.0, 'Outstanding performance in calculus', 'CC1', 2, 3, 3, 1, 'EXAM', NOW(), NOW()),
(4, 15.5, 20.0, 'Solid grasp of linear algebra concepts', 'CC1', 2, 4, 3, 1, 'QUIZ', NOW(), NOW()),
(5, 17.0, 20.0, 'Excellent lab work in physics', 'CC1', 3, 5, 4, 1, 'PROJECT', NOW(), NOW()),
(6, 13.5, 20.0, 'Needs improvement in quantum concepts', 'CC1', 3, 6, 4, 1, 'EXAM', NOW(), NOW()),
(7, 16.0, 20.0, 'Good chemical analysis skills', 'CC1', 4, 7, 5, 1, 'ASSIGNMENT', NOW(), NOW()),
(8, 14.5, 20.0, 'Satisfactory organic chemistry knowledge', 'CC1', 4, 8, 5, 1, 'EXAM', NOW(), NOW()),
(9, 17.5, 20.0, 'Excellent cell biology presentation', 'CC1', 5, 9, 6, 1, 'PROJECT', NOW(), NOW()),
(10, 15.0, 20.0, 'Good genetics problem solving', 'CC1', 5, 10, 6, 1, 'QUIZ', NOW(), NOW());

-- Reset sequences to continue from the last inserted ID
SELECT setval('departments_id_seq', 10);
SELECT setval('semesters_id_seq', 10);
SELECT setval('users_id_seq', 20);
SELECT setval('students_id_seq', 10);
SELECT setval('subject_id_seq', 10);
SELECT setval('grading_window_id_seq', 10);
SELECT setval('grades_id_seq', 10);

-- Default login credentials (password is 'password' for all users):
-- Admin: admin@university.com / password
-- Teachers: [firstname.lastname]@university.com / password
-- Students: [matricule]@student.university.com / password