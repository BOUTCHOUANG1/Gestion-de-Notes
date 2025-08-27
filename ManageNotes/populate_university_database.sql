-- Comprehensive University Database Population
-- Following codebase architecture and university standards

-- 1. Create Admin User
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, phone, department, creation_date, last_modified_date) 
VALUES (1, 'admin', 'System', 'Administrator', 'admin@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'ADMIN', true, false, '+237123456789', 'Administration', NOW(), NOW());

-- 2. Create Teachers (10 teachers across departments)
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, phone, department, creation_date, last_modified_date) VALUES
(2, 'prof.johnson', 'Michael', 'Johnson', 'mjohnson@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456790', 'Computer Science', NOW(), NOW()),
(3, 'prof.williams', 'Sarah', 'Williams', 'swilliams@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456791', 'Mathematics', NOW(), NOW()),
(4, 'prof.brown', 'David', 'Brown', 'dbrown@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456792', 'Physics', NOW(), NOW()),
(5, 'prof.davis', 'Emily', 'Davis', 'edavis@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456793', 'Engineering', NOW(), NOW()),
(6, 'prof.wilson', 'Robert', 'Wilson', 'rwilson@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456794', 'Business Administration', NOW(), NOW()),
(7, 'prof.garcia', 'Maria', 'Garcia', 'mgarcia@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456795', 'Computer Science', NOW(), NOW()),
(8, 'prof.martinez', 'Carlos', 'Martinez', 'cmartinez@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456796', 'Mathematics', NOW(), NOW()),
(9, 'prof.anderson', 'Lisa', 'Anderson', 'landerson@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456797', 'Physics', NOW(), NOW()),
(10, 'prof.taylor', 'James', 'Taylor', 'jtaylor@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456798', 'Engineering', NOW(), NOW()),
(11, 'prof.thomas', 'Jennifer', 'Thomas', 'jthomas@university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'TEACHER', true, false, '+237123456799', 'Business Administration', NOW(), NOW());

-- 3. Create Teacher Levels (assign teachers to levels they can teach)
INSERT INTO user_levels (user_id, level) VALUES
(2, 'LEVEL1'), (2, 'LEVEL2'), (2, 'LEVEL3'), (2, 'LEVEL4'),
(3, 'LEVEL1'), (3, 'LEVEL2'), (3, 'LEVEL3'),
(4, 'LEVEL1'), (4, 'LEVEL2'), (4, 'LEVEL3'),
(5, 'LEVEL3'), (5, 'LEVEL4'),
(6, 'LEVEL1'), (6, 'LEVEL2'),
(7, 'LEVEL2'), (7, 'LEVEL3'), (7, 'LEVEL4'),
(8, 'LEVEL1'), (8, 'LEVEL2'),
(9, 'LEVEL2'), (9, 'LEVEL3'),
(10, 'LEVEL3'), (10, 'LEVEL4'),
(11, 'LEVEL1'), (11, 'LEVEL2'), (11, 'LEVEL3');

-- 4. Assign Teachers to Subjects
UPDATE subject SET id_teacher = 2 WHERE code IN ('CS101', 'CS201', 'CS301', 'CS401');
UPDATE subject SET id_teacher = 3 WHERE code IN ('MATH101', 'MATH201', 'MATH301');
UPDATE subject SET id_teacher = 4 WHERE code IN ('PHYS101', 'PHYS201', 'PHYS301');
UPDATE subject SET id_teacher = 5 WHERE code IN ('ENG301', 'ENG401');
UPDATE subject SET id_teacher = 6 WHERE code IN ('BUS101', 'BUS201');
UPDATE subject SET id_teacher = 7 WHERE code IN ('CS202');
UPDATE subject SET id_teacher = 8 WHERE code IN ('MATH102');
UPDATE subject SET id_teacher = 9 WHERE code IN ('PHYS202');
UPDATE subject SET id_teacher = 10 WHERE code IN ('ENG302');
UPDATE subject SET id_teacher = 11 WHERE code IN ('BUS202', 'BUS301');

-- 5. Create Student Users (50 students across levels)
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, creation_date, last_modified_date) VALUES
-- LEVEL1 Students (15 students)
(12, 'STU2024001', 'Alice', 'Cooper', 'alice.cooper@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(13, 'STU2024002', 'Bob', 'Martin', 'bob.martin@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(14, 'STU2024003', 'Carol', 'White', 'carol.white@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(15, 'STU2024004', 'David', 'Green', 'david.green@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(16, 'STU2024005', 'Eva', 'Black', 'eva.black@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(17, 'STU2024006', 'Frank', 'Blue', 'frank.blue@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(18, 'STU2024007', 'Grace', 'Red', 'grace.red@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(19, 'STU2024008', 'Henry', 'Yellow', 'henry.yellow@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(20, 'STU2024009', 'Iris', 'Purple', 'iris.purple@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(21, 'STU2024010', 'Jack', 'Orange', 'jack.orange@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(22, 'STU2024011', 'Kate', 'Brown', 'kate.brown@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(23, 'STU2024012', 'Leo', 'Gray', 'leo.gray@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(24, 'STU2024013', 'Mia', 'Silver', 'mia.silver@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(25, 'STU2024014', 'Noah', 'Gold', 'noah.gold@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(26, 'STU2024015', 'Olivia', 'Pink', 'olivia.pink@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL2 Students (15 students)
(27, 'STU2023001', 'Paul', 'Cyan', 'paul.cyan@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(28, 'STU2023002', 'Quinn', 'Magenta', 'quinn.magenta@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(29, 'STU2023003', 'Ruby', 'Lime', 'ruby.lime@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(30, 'STU2023004', 'Sam', 'Teal', 'sam.teal@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(31, 'STU2023005', 'Tina', 'Coral', 'tina.coral@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(32, 'STU2023006', 'Uma', 'Ivory', 'uma.ivory@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(33, 'STU2023007', 'Victor', 'Jade', 'victor.jade@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(34, 'STU2023008', 'Wendy', 'Ruby', 'wendy.ruby@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(35, 'STU2023009', 'Xander', 'Amber', 'xander.amber@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(36, 'STU2023010', 'Yara', 'Pearl', 'yara.pearl@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(37, 'STU2023011', 'Zoe', 'Onyx', 'zoe.onyx@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(38, 'STU2023012', 'Adam', 'Copper', 'adam.copper@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(39, 'STU2023013', 'Bella', 'Bronze', 'bella.bronze@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(40, 'STU2023014', 'Chris', 'Steel', 'chris.steel@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(41, 'STU2023015', 'Diana', 'Platinum', 'diana.platinum@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL3 Students (10 students)
(42, 'STU2022001', 'Ethan', 'Diamond', 'ethan.diamond@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(43, 'STU2022002', 'Fiona', 'Emerald', 'fiona.emerald@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(44, 'STU2022003', 'George', 'Sapphire', 'george.sapphire@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(45, 'STU2022004', 'Hannah', 'Topaz', 'hannah.topaz@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(46, 'STU2022005', 'Ian', 'Garnet', 'ian.garnet@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(47, 'STU2022006', 'Julia', 'Opal', 'julia.opal@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(48, 'STU2022007', 'Kevin', 'Quartz', 'kevin.quartz@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(49, 'STU2022008', 'Luna', 'Agate', 'luna.agate@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(50, 'STU2022009', 'Max', 'Jasper', 'max.jasper@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(51, 'STU2022010', 'Nina', 'Turquoise', 'nina.turquoise@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL4 Students (10 students)
(52, 'STU2021001', 'Oscar', 'Obsidian', 'oscar.obsidian@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(53, 'STU2021002', 'Penny', 'Peridot', 'penny.peridot@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(54, 'STU2021003', 'Quinn', 'Citrine', 'quinn.citrine@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(55, 'STU2021004', 'Rose', 'Amethyst', 'rose.amethyst@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(56, 'STU2021005', 'Steve', 'Beryl', 'steve.beryl@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(57, 'STU2021006', 'Tara', 'Carnelian', 'tara.carnelian@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(58, 'STU2021007', 'Ulrich', 'Fluorite', 'ulrich.fluorite@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(59, 'STU2021008', 'Vera', 'Hematite', 'vera.hematite@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(60, 'STU2021009', 'Will', 'Labradorite', 'will.labradorite@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW()),
(61, 'STU2021010', 'Zara', 'Malachite', 'zara.malachite@student.university.edu', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'STUDENT', true, false, NOW(), NOW());

-- 6. Create Student Records
INSERT INTO students (id, first_name, last_name, email, matricule, level, speciality, cycle, date_of_birth, place_of_birth, creation_date, last_modified_date) VALUES
-- LEVEL1 Students
(1, 'Alice', 'Cooper', 'alice.cooper@student.university.edu', 'STU2024001', 'LEVEL1', 'Computer Science', 'BACHELOR', '2005-03-15', 'Yaoundé', NOW(), NOW()),
(2, 'Bob', 'Martin', 'bob.martin@student.university.edu', 'STU2024002', 'LEVEL1', 'Mathematics', 'BACHELOR', '2005-07-22', 'Douala', NOW(), NOW()),
(3, 'Carol', 'White', 'carol.white@student.university.edu', 'STU2024003', 'LEVEL1', 'Physics', 'BACHELOR', '2005-01-10', 'Bamenda', NOW(), NOW()),
(4, 'David', 'Green', 'david.green@student.university.edu', 'STU2024004', 'LEVEL1', 'Computer Science', 'BACHELOR', '2005-04-12', 'Yaoundé', NOW(), NOW()),
(5, 'Eva', 'Black', 'eva.black@student.university.edu', 'STU2024005', 'LEVEL1', 'Mathematics', 'BACHELOR', '2005-08-30', 'Douala', NOW(), NOW()),
(6, 'Frank', 'Blue', 'frank.blue@student.university.edu', 'STU2024006', 'LEVEL1', 'Physics', 'BACHELOR', '2005-02-14', 'Bamenda', NOW(), NOW()),
(7, 'Grace', 'Red', 'grace.red@student.university.edu', 'STU2024007', 'LEVEL1', 'Business Administration', 'BACHELOR', '2005-05-20', 'Yaoundé', NOW(), NOW()),
(8, 'Henry', 'Yellow', 'henry.yellow@student.university.edu', 'STU2024008', 'LEVEL1', 'Computer Science', 'BACHELOR', '2005-09-15', 'Douala', NOW(), NOW()),
(9, 'Iris', 'Purple', 'iris.purple@student.university.edu', 'STU2024009', 'LEVEL1', 'Mathematics', 'BACHELOR', '2005-01-15', 'Yaoundé', NOW(), NOW()),
(10, 'Jack', 'Orange', 'jack.orange@student.university.edu', 'STU2024010', 'LEVEL1', 'Physics', 'BACHELOR', '2005-05-22', 'Douala', NOW(), NOW()),
(11, 'Kate', 'Brown', 'kate.brown@student.university.edu', 'STU2024011', 'LEVEL1', 'Business Administration', 'BACHELOR', '2005-03-08', 'Bamenda', NOW(), NOW()),
(12, 'Leo', 'Gray', 'leo.gray@student.university.edu', 'STU2024012', 'LEVEL1', 'Computer Science', 'BACHELOR', '2005-06-18', 'Yaoundé', NOW(), NOW()),
(13, 'Mia', 'Silver', 'mia.silver@student.university.edu', 'STU2024013', 'LEVEL1', 'Mathematics', 'BACHELOR', '2005-10-25', 'Douala', NOW(), NOW()),
(14, 'Noah', 'Gold', 'noah.gold@student.university.edu', 'STU2024014', 'LEVEL1', 'Physics', 'BACHELOR', '2005-04-03', 'Bamenda', NOW(), NOW()),
(15, 'Olivia', 'Pink', 'olivia.pink@student.university.edu', 'STU2024015', 'LEVEL1', 'Business Administration', 'BACHELOR', '2005-07-11', 'Yaoundé', NOW(), NOW()),
-- LEVEL2 Students
(16, 'Paul', 'Cyan', 'paul.cyan@student.university.edu', 'STU2023001', 'LEVEL2', 'Computer Science', 'BACHELOR', '2004-03-15', 'Yaoundé', NOW(), NOW()),
(17, 'Quinn', 'Magenta', 'quinn.magenta@student.university.edu', 'STU2023002', 'LEVEL2', 'Mathematics', 'BACHELOR', '2004-07-22', 'Douala', NOW(), NOW()),
(18, 'Ruby', 'Lime', 'ruby.lime@student.university.edu', 'STU2023003', 'LEVEL2', 'Physics', 'BACHELOR', '2004-01-10', 'Bamenda', NOW(), NOW()),
(19, 'Sam', 'Teal', 'sam.teal@student.university.edu', 'STU2023004', 'LEVEL2', 'Computer Science', 'BACHELOR', '2004-04-12', 'Yaoundé', NOW(), NOW()),
(20, 'Tina', 'Coral', 'tina.coral@student.university.edu', 'STU2023005', 'LEVEL2', 'Mathematics', 'BACHELOR', '2004-08-30', 'Douala', NOW(), NOW()),
(21, 'Uma', 'Ivory', 'uma.ivory@student.university.edu', 'STU2023006', 'LEVEL2', 'Physics', 'BACHELOR', '2004-02-14', 'Bamenda', NOW(), NOW()),
(22, 'Victor', 'Jade', 'victor.jade@student.university.edu', 'STU2023007', 'LEVEL2', 'Business Administration', 'BACHELOR', '2004-05-20', 'Yaoundé', NOW(), NOW()),
(23, 'Wendy', 'Ruby', 'wendy.ruby@student.university.edu', 'STU2023008', 'LEVEL2', 'Computer Science', 'BACHELOR', '2004-09-15', 'Douala', NOW(), NOW()),
(24, 'Xander', 'Amber', 'xander.amber@student.university.edu', 'STU2023009', 'LEVEL2', 'Mathematics', 'BACHELOR', '2004-01-15', 'Yaoundé', NOW(), NOW()),
(25, 'Yara', 'Pearl', 'yara.pearl@student.university.edu', 'STU2023010', 'LEVEL2', 'Physics', 'BACHELOR', '2004-05-22', 'Douala', NOW(), NOW()),
(26, 'Zoe', 'Onyx', 'zoe.onyx@student.university.edu', 'STU2023011', 'LEVEL2', 'Business Administration', 'BACHELOR', '2004-03-08', 'Bamenda', NOW(), NOW()),
(27, 'Adam', 'Copper', 'adam.copper@student.university.edu', 'STU2023012', 'LEVEL2', 'Computer Science', 'BACHELOR', '2004-06-18', 'Yaoundé', NOW(), NOW()),
(28, 'Bella', 'Bronze', 'bella.bronze@student.university.edu', 'STU2023013', 'LEVEL2', 'Mathematics', 'BACHELOR', '2004-10-25', 'Douala', NOW(), NOW()),
(29, 'Chris', 'Steel', 'chris.steel@student.university.edu', 'STU2023014', 'LEVEL2', 'Physics', 'BACHELOR', '2004-04-03', 'Bamenda', NOW(), NOW()),
(30, 'Diana', 'Platinum', 'diana.platinum@student.university.edu', 'STU2023015', 'LEVEL2', 'Business Administration', 'BACHELOR', '2004-07-11', 'Yaoundé', NOW(), NOW()),
-- LEVEL3 Students
(31, 'Ethan', 'Diamond', 'ethan.diamond@student.university.edu', 'STU2022001', 'LEVEL3', 'Computer Science', 'BACHELOR', '2003-03-15', 'Yaoundé', NOW(), NOW()),
(32, 'Fiona', 'Emerald', 'fiona.emerald@student.university.edu', 'STU2022002', 'LEVEL3', 'Mathematics', 'BACHELOR', '2003-07-22', 'Douala', NOW(), NOW()),
(33, 'George', 'Sapphire', 'george.sapphire@student.university.edu', 'STU2022003', 'LEVEL3', 'Physics', 'BACHELOR', '2003-01-10', 'Bamenda', NOW(), NOW()),
(34, 'Hannah', 'Topaz', 'hannah.topaz@student.university.edu', 'STU2022004', 'LEVEL3', 'Engineering', 'BACHELOR', '2003-04-12', 'Yaoundé', NOW(), NOW()),
(35, 'Ian', 'Garnet', 'ian.garnet@student.university.edu', 'STU2022005', 'LEVEL3', 'Computer Science', 'BACHELOR', '2003-08-30', 'Douala', NOW(), NOW()),
(36, 'Julia', 'Opal', 'julia.opal@student.university.edu', 'STU2022006', 'LEVEL3', 'Mathematics', 'BACHELOR', '2003-02-14', 'Bamenda', NOW(), NOW()),
(37, 'Kevin', 'Quartz', 'kevin.quartz@student.university.edu', 'STU2022007', 'LEVEL3', 'Physics', 'BACHELOR', '2003-05-20', 'Yaoundé', NOW(), NOW()),
(38, 'Luna', 'Agate', 'luna.agate@student.university.edu', 'STU2022008', 'LEVEL3', 'Engineering', 'BACHELOR', '2003-09-15', 'Douala', NOW(), NOW()),
(39, 'Max', 'Jasper', 'max.jasper@student.university.edu', 'STU2022009', 'LEVEL3', 'Computer Science', 'BACHELOR', '2003-01-15', 'Yaoundé', NOW(), NOW()),
(40, 'Nina', 'Turquoise', 'nina.turquoise@student.university.edu', 'STU2022010', 'LEVEL3', 'Business Administration', 'BACHELOR', '2003-05-22', 'Douala', NOW(), NOW()),
-- LEVEL4 Students
(41, 'Oscar', 'Obsidian', 'oscar.obsidian@student.university.edu', 'STU2021001', 'LEVEL4', 'Computer Science', 'MASTER', '2002-03-15', 'Yaoundé', NOW(), NOW()),
(42, 'Penny', 'Peridot', 'penny.peridot@student.university.edu', 'STU2021002', 'LEVEL4', 'Engineering', 'MASTER', '2002-07-22', 'Douala', NOW(), NOW()),
(43, 'Quinn', 'Citrine', 'quinn.citrine@student.university.edu', 'STU2021003', 'LEVEL4', 'Computer Science', 'MASTER', '2002-01-10', 'Bamenda', NOW(), NOW()),
(44, 'Rose', 'Amethyst', 'rose.amethyst@student.university.edu', 'STU2021004', 'LEVEL4', 'Engineering', 'MASTER', '2002-04-12', 'Yaoundé', NOW(), NOW()),
(45, 'Steve', 'Beryl', 'steve.beryl@student.university.edu', 'STU2021005', 'LEVEL4', 'Computer Science', 'MASTER', '2002-08-30', 'Douala', NOW(), NOW()),
(46, 'Tara', 'Carnelian', 'tara.carnelian@student.university.edu', 'STU2021006', 'LEVEL4', 'Engineering', 'MASTER', '2002-02-14', 'Bamenda', NOW(), NOW()),
(47, 'Ulrich', 'Fluorite', 'ulrich.fluorite@student.university.edu', 'STU2021007', 'LEVEL4', 'Computer Science', 'MASTER', '2002-05-20', 'Yaoundé', NOW(), NOW()),
(48, 'Vera', 'Hematite', 'vera.hematite@student.university.edu', 'STU2021008', 'LEVEL4', 'Engineering', 'MASTER', '2002-09-15', 'Douala', NOW(), NOW()),
(49, 'Will', 'Labradorite', 'will.labradorite@student.university.edu', 'STU2021009', 'LEVEL4', 'Computer Science', 'MASTER', '2002-01-15', 'Yaoundé', NOW(), NOW()),
(50, 'Zara', 'Malachite', 'zara.malachite@student.university.edu', 'STU2021010', 'LEVEL4', 'Engineering', 'MASTER', '2002-05-22', 'Douala', NOW(), NOW());

-- 7. Update sequences
SELECT setval('users_seq', 61);
SELECT setval('students_seq', 50);

SELECT 'University database populated successfully!' as message;
SELECT 'Users: ' || count(*) as users_count FROM users;
SELECT 'Students: ' || count(*) as students_count FROM students;
SELECT 'Subjects with teachers: ' || count(*) as subjects_with_teachers FROM subject WHERE id_teacher IS NOT NULL;