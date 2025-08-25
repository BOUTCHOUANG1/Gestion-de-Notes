-- Set proper passwords for database users
-- BCrypt hash for "nathan": $2a$10$N.zmdr9k7uOsaLQJlXpOUeUtIZSHLoR3CYQFdQ3OIqQqrtwWh2.Pu

UPDATE users SET password = '$2a$10$N.zmdr9k7uOsaLQJlXpOUeUtIZSHLoR3CYQFdQ3OIqQqrtwWh2.Pu' 
WHERE username IN ('prof.johnson', 'prof.williams', 'prof.brown', 'prof.davis', 'alice.cooper', 'bob.martin', 'carol.white', 'daniel.green', 'eva.black', 'frank.blue', 'grace.red', 'henry.yellow', 'iris.purple', 'jack.orange');

SELECT 'Database user passwords set to "nathan"!' as message;