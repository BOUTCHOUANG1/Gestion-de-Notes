-- Set admin password to "admin" using proper BCrypt
UPDATE users SET password = '$2a$10$dXJ3SW6G7P2lxUOWuNlb5OmQJk9hgO.h2/VhJkCr1cqGeJQYHdBJO' WHERE username = 'admin';

SELECT 'Admin password set!' as message;