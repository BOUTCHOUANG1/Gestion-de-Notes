-- Delete existing admin and create a fresh one
DELETE FROM users WHERE username = 'admin';

-- Insert admin with a simple password hash for "test123"
INSERT INTO users (id, username, first_name, last_name, email, password, role, active, must_change_password, creation_date, last_modified_date) 
VALUES (1, 'admin', 'Admin', 'User', 'admin@test.com', '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO', 'ADMIN', true, false, NOW(), NOW());

SELECT 'Test admin user created!' as message;
SELECT username, role, active FROM users WHERE username = 'admin';