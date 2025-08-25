-- Remove unused tables that don't exist in the codebase
DROP TABLE IF EXISTS invite_token CASCADE;
DROP TABLE IF EXISTS student_info_requests CASCADE;
DROP TABLE IF EXISTS grade_report CASCADE;
DROP TABLE IF EXISTS report_record CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS invite_token_id_seq;

SELECT 'Unused tables cleaned up!' as message;