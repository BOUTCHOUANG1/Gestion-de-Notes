-- Fix Academic Year 2025/2026 with proper dates and revendication periods

-- Step 1: Update existing semesters with correct dates for 2025/2026
UPDATE semester SET 
    name = 'Semester 1 - 2025/2026',
    start_date = '2025-09-08',
    end_date = '2026-02-20',
    is_active = true
WHERE semester_id = 1;

UPDATE semester SET 
    name = 'Semester 2 - 2025/2026',
    start_date = '2026-03-02',
    end_date = '2026-07-31',
    is_active = false
WHERE semester_id = 2;

-- Step 2: Delete existing revendication periods
DELETE FROM revendication_period;

-- Step 3: Create revendication periods for Semester 1 (2025/2026)
-- CC1 revendication period (after CC1 exams in October)
INSERT INTO revendication_period (start_date, end_date, color, is_active, exam_period_id, semester_id, creation_date, last_modified_date)
VALUES 
('2025-10-20', '2025-10-27', '#FF5733', true, 1, 1, NOW(), NOW());

-- SN1 revendication period (after SN1 exams in December)
INSERT INTO revendication_period (start_date, end_date, color, is_active, exam_period_id, semester_id, creation_date, last_modified_date)
VALUES 
('2025-12-20', '2025-12-27', '#33FF57', true, 3, 1, NOW(), NOW());

-- Step 4: Create revendication periods for Semester 2 (2025/2026)
-- CC2 revendication period (after CC2 exams in April)
INSERT INTO revendication_period (start_date, end_date, color, is_active, exam_period_id, semester_id, creation_date, last_modified_date)
VALUES 
('2026-04-20', '2026-04-27', '#3357FF', false, 2, 2, NOW(), NOW());

-- SN2 revendication period (after SN2 exams in June)
INSERT INTO revendication_period (start_date, end_date, color, is_active, exam_period_id, semester_id, creation_date, last_modified_date)
VALUES 
('2026-06-20', '2026-06-27', '#FF33F5', false, 4, 2, NOW(), NOW());

-- Verify the changes
SELECT 'Semesters:' as info;
SELECT semester_id, name, start_date, end_date, is_active FROM semester ORDER BY semester_id;

SELECT 'Revendication Periods:' as info;
SELECT rp.revendication_period_id, rp.start_date, rp.end_date, rp.color, rp.is_active, 
       ep.assessment_type as exam_type, s.name as semester_name
FROM revendication_period rp
JOIN exam_periods ep ON rp.exam_period_id = ep.exam_period_id
JOIN semester s ON rp.semester_id = s.semester_id
ORDER BY rp.semester_id, ep.exam_period_id;
