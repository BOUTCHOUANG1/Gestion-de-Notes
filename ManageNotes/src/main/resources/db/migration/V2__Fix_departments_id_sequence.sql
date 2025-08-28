-- Fix departments table to use auto-increment ID like other tables
ALTER TABLE departments ALTER COLUMN id SET DEFAULT nextval('departments_seq');

-- Reset the sequence to start from the next available ID
SELECT setval('departments_seq', COALESCE((SELECT MAX(id) FROM departments), 0) + 1, false);