-- Safe migration for Teacher Level Constraint
-- This script handles existing data conflicts more carefully

BEGIN;

-- Step 1: Show current conflicts
SELECT 
    id_teacher, 
    level, 
    COUNT(*) as subject_count,
    STRING_AGG(name || ' (ID: ' || id || ')', ', ') as subjects
FROM subject 
WHERE id_teacher IS NOT NULL 
GROUP BY id_teacher, level 
HAVING COUNT(*) > 1
ORDER BY id_teacher, level;

-- Step 2: For each conflict, keep the subject with the most recent grades
-- or the one with the highest ID if no grades exist

-- Create a temporary table to identify subjects to keep
CREATE TEMP TABLE subjects_to_keep AS
WITH ranked_subjects AS (
    SELECT 
        s.id,
        s.id_teacher,
        s.level,
        s.name,
        COALESCE(MAX(g.creation_date), s.creation_date) as latest_activity,
        COUNT(g.id) as grade_count,
        ROW_NUMBER() OVER (
            PARTITION BY s.id_teacher, s.level 
            ORDER BY COUNT(g.id) DESC, COALESCE(MAX(g.creation_date), s.creation_date) DESC, s.id DESC
        ) as rn
    FROM subject s
    LEFT JOIN grades g ON s.id = g.id_subject
    WHERE s.id_teacher IS NOT NULL
    GROUP BY s.id, s.id_teacher, s.level, s.name, s.creation_date
)
SELECT id, id_teacher, level, name
FROM ranked_subjects 
WHERE rn = 1;

-- Step 3: Show which subjects will be kept
SELECT 
    'KEEPING: ' || name || ' (ID: ' || id || ') for Teacher ' || id_teacher || ' at ' || level as action
FROM subjects_to_keep
WHERE (id_teacher, level) IN (
    SELECT id_teacher, level 
    FROM subject 
    WHERE id_teacher IS NOT NULL 
    GROUP BY id_teacher, level 
    HAVING COUNT(*) > 1
)
ORDER BY id_teacher, level;

-- Step 4: Unassign teachers from subjects that will be removed (don't delete subjects)
-- This preserves the subjects but removes the teacher assignment
UPDATE subject 
SET id_teacher = NULL
WHERE id_teacher IS NOT NULL 
AND id NOT IN (SELECT id FROM subjects_to_keep)
AND (id_teacher, level) IN (
    SELECT id_teacher, level 
    FROM subject 
    WHERE id_teacher IS NOT NULL 
    GROUP BY id_teacher, level 
    HAVING COUNT(*) > 1
);

-- Step 5: Show the changes made
SELECT 
    'UNASSIGNED: ' || name || ' (ID: ' || id || ') - Teacher removed from ' || level as action
FROM subject
WHERE id_teacher IS NULL 
AND id NOT IN (SELECT id FROM subjects_to_keep);

-- Step 6: Add the unique constraint
ALTER TABLE subject 
ADD CONSTRAINT uk_teacher_level 
UNIQUE (id_teacher, level);

-- Step 7: Verify the constraint was added
SELECT 
    conname as constraint_name,
    contype as constraint_type,
    pg_get_constraintdef(oid) as constraint_definition
FROM pg_constraint 
WHERE conname = 'uk_teacher_level';

-- Step 8: Show final teacher assignments
SELECT 
    u.first_name || ' ' || u.last_name as teacher_name,
    s.level,
    s.name as subject_name,
    s.code as subject_code
FROM subject s
JOIN users u ON s.id_teacher = u.id
WHERE s.id_teacher IS NOT NULL
ORDER BY u.first_name, u.last_name, s.level;

COMMIT;