-- Fix for Teacher Level Constraint
-- This script adds the unique constraint to ensure a teacher can only teach one subject per level

-- Step 1: Identify and resolve any existing conflicts
-- Show current conflicts (if any)
SELECT 
    id_teacher, 
    level, 
    COUNT(*) as subject_count,
    STRING_AGG(name, ', ') as subjects
FROM subject 
WHERE id_teacher IS NOT NULL 
GROUP BY id_teacher, level 
HAVING COUNT(*) > 1;

-- Step 2: Remove duplicate assignments (keep the most recent one)
-- This will delete older subject assignments for the same teacher-level combination
DELETE FROM subject s1 
WHERE s1.id NOT IN (
    SELECT MAX(s2.id) 
    FROM subject s2 
    WHERE s2.id_teacher = s1.id_teacher 
    AND s2.level = s1.level 
    AND s2.id_teacher IS NOT NULL
    GROUP BY s2.id_teacher, s2.level
);

-- Step 3: Add the unique constraint
ALTER TABLE subject 
ADD CONSTRAINT uk_teacher_level 
UNIQUE (id_teacher, level);

-- Step 4: Verify the constraint was added
SELECT 
    conname as constraint_name,
    contype as constraint_type,
    pg_get_constraintdef(oid) as constraint_definition
FROM pg_constraint 
WHERE conname = 'uk_teacher_level';

-- Step 5: Test the constraint (this should fail if constraint is working)
-- INSERT INTO subject (name, code, credits, level, id_teacher, department_id, id_semester) 
-- VALUES ('Test Subject', 'TEST001', 3, 'LEVEL1', 1, 1, 1);
-- INSERT INTO subject (name, code, credits, level, id_teacher, department_id, id_semester) 
-- VALUES ('Test Subject 2', 'TEST002', 3, 'LEVEL1', 1, 1, 1); -- This should fail

COMMIT;