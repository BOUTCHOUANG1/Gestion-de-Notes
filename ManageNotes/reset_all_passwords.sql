-- Reset all passwords to a known working BCrypt hash
-- This hash corresponds to password "123456"
UPDATE users SET password = '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO';

SELECT 'All passwords reset to "123456"' as message;
SELECT username, role FROM users ORDER BY username;