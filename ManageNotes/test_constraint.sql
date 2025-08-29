-- Test script to verify the teacher-level constraint works

-- Test 1: Try to assign the same teacher to two subjects at the same level
-- This should fail with constraint violation

BEGIN;

-- First, let's see current teacher assignments
SELECT 
    u.first_name || ' ' || u.last_name as teacher_name,
    s.level,
    s.name as subject_name,
    s.code as subject_code
FROM subject s
JOIN users u ON s.id_teacher = u.id
WHERE s.id_teacher IS NOT NULL
ORDER BY u.first_name, u.last_name, s.level;

-- Test 2: Try to insert a duplicate teacher-level assignment
-- This should fail
INSERT INTO subject (name, code, credits, level, cycle, id_teacher, department_id, id_semester, active, creation_date, last_modified_date) 
VALUES ('Test Duplicate Subject', 'TEST001', 3, 'LEVEL1', 'BACHELOR', 2, 1, 1, true, NOW(), NOW());

ROLLBACK;