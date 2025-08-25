-- Add User accounts for Students
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, creation_date, last_modified_date) VALUES
-- LEVEL1 Students
(6, 'alice.cooper', 'Alice', 'Cooper', 'alice.cooper@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(7, 'bob.martin', 'Bob', 'Martin', 'bob.martin@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(8, 'carol.white', 'Carol', 'White', 'carol.white@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL2 Students
(9, 'daniel.green', 'Daniel', 'Green', 'daniel.green@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(10, 'eva.black', 'Eva', 'Black', 'eva.black@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(11, 'frank.blue', 'Frank', 'Blue', 'frank.blue@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL3 Students
(12, 'grace.red', 'Grace', 'Red', 'grace.red@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(13, 'henry.yellow', 'Henry', 'Yellow', 'henry.yellow@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
-- LEVEL4 Students
(14, 'iris.purple', 'Iris', 'Purple', 'iris.purple@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW()),
(15, 'jack.orange', 'Jack', 'Orange', 'jack.orange@student.university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'STUDENT', true, false, NOW(), NOW());

SELECT 'Student login accounts created!' as message;
SELECT 'Total Users: ' || COUNT(*) FROM users;