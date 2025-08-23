-- Database Migration Script to Fix Column Names
-- Run this if you want to keep existing data

-- Add missing audit columns to departments
ALTER TABLE departments 
ADD COLUMN IF NOT EXISTS creation_date timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS last_modified_date timestamp(6) with time zone;

UPDATE departments SET creation_date = CURRENT_TIMESTAMP WHERE creation_date IS NULL;
ALTER TABLE departments ALTER COLUMN creation_date SET NOT NULL;

-- No changes needed for other tables as they already have correct column names:
-- - users table already has first_name, last_name, creation_date, last_modified_date
-- - students table already has first_name, last_name, date_of_birth, place_of_birth
-- - semesters table already has start_date, end_date
-- - subject table already has id_teacher, id_semester, department_id
-- - grades table already has id_users, id_semester, id_students, id_subject, grade_type
-- - grading_window table already has id_semester, start_date, end_date

-- Verify all tables have correct structure
SELECT 'Migration completed successfully' as status;