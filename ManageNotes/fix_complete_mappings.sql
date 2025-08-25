-- Fix complete teacher-level mappings based on actual subject assignments

DELETE FROM user_levels;

INSERT INTO user_levels (user_id, level) VALUES
-- prof.johnson (Computer Science) - teaches LEVEL1, LEVEL2, LEVEL3, LEVEL4
(2, 'LEVEL1'), (2, 'LEVEL2'), (2, 'LEVEL3'), (2, 'LEVEL4'),
-- prof.williams (Mathematics) - teaches LEVEL1, LEVEL2
(3, 'LEVEL1'), (3, 'LEVEL2'),
-- prof.brown (Physics) - teaches LEVEL1, LEVEL2
(4, 'LEVEL1'), (4, 'LEVEL2'),
-- prof.davis (Engineering) - teaches LEVEL3, LEVEL4
(5, 'LEVEL3'), (5, 'LEVEL4');

-- Verify mappings
SELECT 'Fixed teacher-subject-level mappings:' as status;
SELECT u.username, u.department, 
       array_agg(DISTINCT ul.level ORDER BY ul.level) as can_teach_levels,
       array_agg(DISTINCT s.level ORDER BY s.level) as assigned_subject_levels
FROM users u 
LEFT JOIN user_levels ul ON u.id = ul.user_id 
LEFT JOIN subject s ON u.id = s.id_teacher 
WHERE u.role = 'TEACHER' 
GROUP BY u.username, u.department 
ORDER BY u.username;