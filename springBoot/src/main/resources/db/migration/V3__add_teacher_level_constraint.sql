-- Add unique constraint to ensure a teacher can only teach one subject per level
-- First, remove any duplicate assignments if they exist
DELETE FROM subject s1 
WHERE s1.id NOT IN (
    SELECT MIN(s2.id) 
    FROM subject s2 
    WHERE s2.id_teacher = s1.id_teacher 
    AND s2.level = s1.level 
    AND s2.id_teacher IS NOT NULL
);

-- Add the unique constraint
ALTER TABLE subject 
ADD CONSTRAINT uk_teacher_level 
UNIQUE (id_teacher, level);