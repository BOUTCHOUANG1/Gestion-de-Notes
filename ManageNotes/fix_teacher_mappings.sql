-- Fix teacher-level mappings to match their assigned subjects

-- Clear existing teacher levels
DELETE FROM user_levels;

-- Add correct teacher levels based on their assigned subjects
INSERT INTO user_levels (user_id, level) VALUES
-- prof.johnson (Computer Science) - teaches LEVEL1-LEVEL4
(2, 'LEVEL1'), (2, 'LEVEL2'), (2, 'LEVEL3'), (2, 'LEVEL4'),
-- prof.williams (Mathematics) - teaches LEVEL1-LEVEL2  
(3, 'LEVEL1'), (3, 'LEVEL2'),
-- prof.brown (Physics) - teaches LEVEL1-LEVEL2
(4, 'LEVEL1'), (4, 'LEVEL2'),
-- prof.davis (Engineering) - teaches LEVEL3-LEVEL4
(5, 'LEVEL3'), (5, 'LEVEL4');

SELECT 'Teacher-level mappings fixed!' as message;