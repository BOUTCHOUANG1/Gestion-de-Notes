-- Fix user passwords to properly encode "nathan"
-- BCrypt hash for "nathan" is: $2a$10$N.zmdr9k7uOsaLQJlXpOUeUtIZSHLoR3CYQFdQ3OIqQqrtwWh2.Pu

UPDATE users SET password = '$2a$10$N.zmdr9k7uOsaLQJlXpOUeUtIZSHLoR3CYQFdQ3OIqQqrtwWh2.Pu' WHERE role IN ('ADMIN', 'TEACHER', 'STUDENT');

SELECT 'Passwords fixed for all users!' as message;
SELECT username, role FROM users;