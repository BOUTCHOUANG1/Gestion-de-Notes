-- Complete all grading periods for full academic year testing

-- Add SN_1 grades (Final exams for S1)
INSERT INTO grades (id, value, max_value, comments, period_label, id_students, id_subject, id_users, id_semester, grade_type, creation_date, last_modified_date) VALUES
-- Alice Cooper (LEVEL1) - SN_1 grades
(24, 14.0, 20.0, 'Final exam performance', 'SN_1', 1, 1, 2, 1, 'SN', NOW(), NOW()),
(25, 13.5, 20.0, 'Calculus final exam', 'SN_1', 1, 2, 3, 1, 'SN', NOW(), NOW()),
(26, 15.5, 20.0, 'Physics final exam', 'SN_1', 1, 3, 4, 1, 'SN', NOW(), NOW()),

-- Bob Martin (LEVEL1) - SN_1 grades  
(27, 18.5, 20.0, 'Excellent final exam', 'SN_1', 2, 1, 2, 1, 'SN', NOW(), NOW()),
(28, 19.0, 20.0, 'Outstanding math final', 'SN_1', 2, 2, 3, 1, 'SN', NOW(), NOW()),
(29, 16.0, 20.0, 'Good physics final', 'SN_1', 2, 3, 4, 1, 'SN', NOW(), NOW()),

-- Carol White (LEVEL1) - SN_1 grades
(30, 12.0, 20.0, 'Needs improvement', 'SN_1', 3, 1, 2, 1, 'SN', NOW(), NOW()),
(31, 14.5, 20.0, 'Better calculus final', 'SN_1', 3, 2, 3, 1, 'SN', NOW(), NOW()),
(32, 18.0, 20.0, 'Excellent physics final', 'SN_1', 3, 3, 4, 1, 'SN', NOW(), NOW()),

-- Daniel Green (LEVEL2) - SN_1 grades
(33, 15.5, 20.0, 'Good data structures final', 'SN_1', 4, 4, 2, 1, 'SN', NOW(), NOW()),
(34, 14.0, 20.0, 'Linear algebra final', 'SN_1', 4, 5, 3, 1, 'SN', NOW(), NOW()),
(35, 13.5, 20.0, 'Physics final exam', 'SN_1', 4, 6, 4, 1, 'SN', NOW(), NOW()),

-- Eva Black (LEVEL2) - SN_1 grades
(36, 18.0, 20.0, 'Excellent final performance', 'SN_1', 5, 4, 2, 1, 'SN', NOW(), NOW()),
(37, 19.5, 20.0, 'Outstanding math final', 'SN_1', 5, 5, 3, 1, 'SN', NOW(), NOW()),
(38, 17.0, 20.0, 'Very good physics final', 'SN_1', 5, 6, 4, 1, 'SN', NOW(), NOW()),

-- Grace Red (LEVEL3) - SN_1 grades
(39, 19.0, 20.0, 'Exceptional algorithms final', 'SN_1', 7, 7, 2, 1, 'SN', NOW(), NOW()),
(40, 17.5, 20.0, 'Good thermodynamics final', 'SN_1', 7, 8, 5, 1, 'SN', NOW(), NOW()),

-- Henry Yellow (LEVEL3) - SN_1 grades
(41, 14.5, 20.0, 'Algorithm final improving', 'SN_1', 8, 7, 2, 1, 'SN', NOW(), NOW()),
(42, 18.5, 20.0, 'Excellent thermodynamics final', 'SN_1', 8, 8, 5, 1, 'SN', NOW(), NOW()),

-- Iris Purple (LEVEL4) - SN_1 grades
(43, 17.5, 20.0, 'Good ML final exam', 'SN_1', 9, 9, 2, 1, 'SN', NOW(), NOW()),
(44, 16.5, 20.0, 'Advanced mechanics final', 'SN_1', 9, 10, 5, 1, 'SN', NOW(), NOW()),

-- Jack Orange (LEVEL4) - SN_1 grades
(45, 16.0, 20.0, 'ML final exam progress', 'SN_1', 10, 9, 2, 1, 'SN', NOW(), NOW()),
(46, 19.0, 20.0, 'Outstanding mechanics final', 'SN_1', 10, 10, 5, 1, 'SN', NOW(), NOW());

-- Add subjects for S2 2024
INSERT INTO subject (id, name, code, credits, id_teacher, description, level, cycle, id_semester, department_id, active, creation_date, last_modified_date) VALUES
-- LEVEL1 Subjects for S2
(11, 'Object-Oriented Programming', 'CS102', 4.0, 2, 'OOP concepts using Java', 'LEVEL1', 'BACHELOR', 2, 1, true, NOW(), NOW()),
(12, 'Calculus II', 'MATH102', 3.0, 3, 'Advanced calculus and series', 'LEVEL1', 'BACHELOR', 2, 2, true, NOW(), NOW()),
(13, 'Physics II', 'PHYS102', 3.0, 4, 'Waves and optics', 'LEVEL1', 'BACHELOR', 2, 3, true, NOW(), NOW()),
-- LEVEL2 Subjects for S2
(14, 'Database Systems', 'CS202', 4.0, 2, 'Database design and SQL', 'LEVEL2', 'BACHELOR', 2, 1, true, NOW(), NOW()),
(15, 'Statistics', 'MATH202', 3.0, 3, 'Probability and statistics', 'LEVEL2', 'BACHELOR', 2, 2, true, NOW(), NOW()),
(16, 'Modern Physics', 'PHYS202', 3.0, 4, 'Quantum and relativity basics', 'LEVEL2', 'BACHELOR', 2, 3, true, NOW(), NOW()),
-- LEVEL3 Subjects for S2
(17, 'Software Engineering', 'CS302', 4.0, 2, 'Software development lifecycle', 'LEVEL3', 'BACHELOR', 2, 1, true, NOW(), NOW()),
(18, 'Fluid Mechanics', 'ENG302', 3.0, 5, 'Fluid flow and dynamics', 'LEVEL3', 'BACHELOR', 2, 4, true, NOW(), NOW()),
-- LEVEL4 Subjects for S2
(19, 'Deep Learning', 'CS402', 4.0, 2, 'Neural networks and AI', 'LEVEL4', 'MASTER', 2, 1, true, NOW(), NOW()),
(20, 'Control Systems', 'ENG402', 3.0, 5, 'Automatic control theory', 'LEVEL4', 'MASTER', 2, 4, true, NOW(), NOW());

-- Add CC_2 grades (Continuous Assessment for S2)
INSERT INTO grades (id, value, max_value, comments, period_label, id_students, id_subject, id_users, id_semester, grade_type, creation_date, last_modified_date) VALUES
-- Alice Cooper (LEVEL1) - CC_2 grades
(47, 16.0, 20.0, 'Good OOP understanding', 'CC_2', 1, 11, 2, 2, 'CC', NOW(), NOW()),
(48, 15.0, 20.0, 'Calculus II progress', 'CC_2', 1, 12, 3, 2, 'CC', NOW(), NOW()),
(49, 17.0, 20.0, 'Physics II improvement', 'CC_2', 1, 13, 4, 2, 'CC', NOW(), NOW()),

-- Bob Martin (LEVEL1) - CC_2 grades
(50, 18.0, 20.0, 'Excellent OOP skills', 'CC_2', 2, 11, 2, 2, 'CC', NOW(), NOW()),
(51, 19.0, 20.0, 'Outstanding calculus', 'CC_2', 2, 12, 3, 2, 'CC', NOW(), NOW()),
(52, 16.5, 20.0, 'Good physics work', 'CC_2', 2, 13, 4, 2, 'CC', NOW(), NOW()),

-- Carol White (LEVEL1) - CC_2 grades
(53, 14.0, 20.0, 'OOP concepts developing', 'CC_2', 3, 11, 2, 2, 'CC', NOW(), NOW()),
(54, 16.0, 20.0, 'Better calculus performance', 'CC_2', 3, 12, 3, 2, 'CC', NOW(), NOW()),
(55, 18.5, 20.0, 'Excellent physics aptitude', 'CC_2', 3, 13, 4, 2, 'CC', NOW(), NOW()),

-- Daniel Green (LEVEL2) - CC_2 grades
(56, 17.0, 20.0, 'Good database knowledge', 'CC_2', 4, 14, 2, 2, 'CC', NOW(), NOW()),
(57, 15.5, 20.0, 'Statistics understanding', 'CC_2', 4, 15, 3, 2, 'CC', NOW(), NOW()),
(58, 14.0, 20.0, 'Modern physics concepts', 'CC_2', 4, 16, 4, 2, 'CC', NOW(), NOW()),

-- Eva Black (LEVEL2) - CC_2 grades
(59, 18.5, 20.0, 'Excellent database work', 'CC_2', 5, 14, 2, 2, 'CC', NOW(), NOW()),
(60, 19.0, 20.0, 'Outstanding statistics', 'CC_2', 5, 15, 3, 2, 'CC', NOW(), NOW()),
(61, 17.5, 20.0, 'Very good modern physics', 'CC_2', 5, 16, 4, 2, 'CC', NOW(), NOW()),

-- Grace Red (LEVEL3) - CC_2 grades
(62, 19.0, 20.0, 'Exceptional software engineering', 'CC_2', 7, 17, 2, 2, 'CC', NOW(), NOW()),
(63, 17.0, 20.0, 'Good fluid mechanics', 'CC_2', 7, 18, 5, 2, 'CC', NOW(), NOW()),

-- Henry Yellow (LEVEL3) - CC_2 grades
(64, 15.5, 20.0, 'Software engineering progress', 'CC_2', 8, 17, 2, 2, 'CC', NOW(), NOW()),
(65, 18.0, 20.0, 'Strong fluid mechanics', 'CC_2', 8, 18, 5, 2, 'CC', NOW(), NOW()),

-- Iris Purple (LEVEL4) - CC_2 grades
(66, 17.5, 20.0, 'Excellent deep learning', 'CC_2', 9, 19, 2, 2, 'CC', NOW(), NOW()),
(67, 16.0, 20.0, 'Good control systems', 'CC_2', 9, 20, 5, 2, 'CC', NOW(), NOW()),

-- Jack Orange (LEVEL4) - CC_2 grades
(68, 16.5, 20.0, 'Deep learning improving', 'CC_2', 10, 19, 2, 2, 'CC', NOW(), NOW()),
(69, 18.5, 20.0, 'Outstanding control systems', 'CC_2', 10, 20, 5, 2, 'CC', NOW(), NOW());

-- Add SN_2 grades (Final exams for S2)
INSERT INTO grades (id, value, max_value, comments, period_label, id_students, id_subject, id_users, id_semester, grade_type, creation_date, last_modified_date) VALUES
-- Alice Cooper (LEVEL1) - SN_2 grades
(70, 15.0, 20.0, 'OOP final exam', 'SN_2', 1, 11, 2, 2, 'SN', NOW(), NOW()),
(71, 14.0, 20.0, 'Calculus II final', 'SN_2', 1, 12, 3, 2, 'SN', NOW(), NOW()),
(72, 16.5, 20.0, 'Physics II final', 'SN_2', 1, 13, 4, 2, 'SN', NOW(), NOW()),

-- Bob Martin (LEVEL1) - SN_2 grades
(73, 19.0, 20.0, 'Excellent OOP final', 'SN_2', 2, 11, 2, 2, 'SN', NOW(), NOW()),
(74, 18.5, 20.0, 'Outstanding calculus final', 'SN_2', 2, 12, 3, 2, 'SN', NOW(), NOW()),
(75, 17.0, 20.0, 'Good physics final', 'SN_2', 2, 13, 4, 2, 'SN', NOW(), NOW()),

-- Carol White (LEVEL1) - SN_2 grades
(76, 13.0, 20.0, 'OOP final needs work', 'SN_2', 3, 11, 2, 2, 'SN', NOW(), NOW()),
(77, 15.5, 20.0, 'Better calculus final', 'SN_2', 3, 12, 3, 2, 'SN', NOW(), NOW()),
(78, 19.0, 20.0, 'Excellent physics final', 'SN_2', 3, 13, 4, 2, 'SN', NOW(), NOW()),

-- Daniel Green (LEVEL2) - SN_2 grades
(79, 16.0, 20.0, 'Good database final', 'SN_2', 4, 14, 2, 2, 'SN', NOW(), NOW()),
(80, 14.5, 20.0, 'Statistics final exam', 'SN_2', 4, 15, 3, 2, 'SN', NOW(), NOW()),
(81, 13.0, 20.0, 'Modern physics final', 'SN_2', 4, 16, 4, 2, 'SN', NOW(), NOW()),

-- Eva Black (LEVEL2) - SN_2 grades
(82, 19.0, 20.0, 'Excellent database final', 'SN_2', 5, 14, 2, 2, 'SN', NOW(), NOW()),
(83, 18.5, 20.0, 'Outstanding statistics final', 'SN_2', 5, 15, 3, 2, 'SN', NOW(), NOW()),
(84, 18.0, 20.0, 'Very good physics final', 'SN_2', 5, 16, 4, 2, 'SN', NOW(), NOW()),

-- Grace Red (LEVEL3) - SN_2 grades
(85, 18.5, 20.0, 'Exceptional software final', 'SN_2', 7, 17, 2, 2, 'SN', NOW(), NOW()),
(86, 17.5, 20.0, 'Good fluid mechanics final', 'SN_2', 7, 18, 5, 2, 'SN', NOW(), NOW()),

-- Henry Yellow (LEVEL3) - SN_2 grades
(87, 15.0, 20.0, 'Software engineering final', 'SN_2', 8, 17, 2, 2, 'SN', NOW(), NOW()),
(88, 19.0, 20.0, 'Outstanding fluid final', 'SN_2', 8, 18, 5, 2, 'SN', NOW(), NOW()),

-- Iris Purple (LEVEL4) - SN_2 grades
(89, 18.0, 20.0, 'Excellent deep learning final', 'SN_2', 9, 19, 2, 2, 'SN', NOW(), NOW()),
(90, 16.5, 20.0, 'Good control systems final', 'SN_2', 9, 20, 5, 2, 'SN', NOW(), NOW()),

-- Jack Orange (LEVEL4) - SN_2 grades
(91, 17.0, 20.0, 'Deep learning final progress', 'SN_2', 10, 19, 2, 2, 'SN', NOW(), NOW()),
(92, 19.5, 20.0, 'Outstanding control final', 'SN_2', 10, 20, 5, 2, 'SN', NOW(), NOW());

SELECT 'Complete academic year grades added!' as message;
SELECT 'Total Grades: ' || COUNT(*) FROM grades;