-- Migration script to update grading_window table structure
-- Run this after dropping and recreating the database

-- The table will be recreated by Hibernate with the new structure
-- This script is for reference and manual data migration if needed

-- Expected new table structure:
/*
CREATE TABLE public.grading_window (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    name character varying(255),
    short_name character varying(255),
    type character varying(255),
    start_date date,
    end_date date,
    color character varying(255),
    is_active boolean,
    order_index integer,
    id_semester bigint,
    CONSTRAINT grading_window_type_check CHECK (((type)::text = ANY ((ARRAY['CC'::character varying, 'SN'::character varying])::text[])))
);
*/

-- Sample data insertion (will be handled by the initialize endpoint)
-- This is just for reference
/*
INSERT INTO grading_window (id, creation_date, name, short_name, type, start_date, end_date, color, is_active, order_index, id_semester) VALUES
(1, NOW(), 'Contrôle continu #1', 'CC #1', 'CC', '2025-10-01', '2025-11-28', '#FF8A95', false, 1, 1),
(2, NOW(), 'Session normale #1', 'SN #1', 'SN', '2025-11-28', '2026-02-02', '#FFB366', false, 2, 1),
(3, NOW(), 'Contrôle continu #2', 'CC #2', 'CC', '2026-02-02', '2026-03-15', '#B19CD9', false, 3, 2),
(4, NOW(), 'Session normale #2', 'SN #2', 'SN', '2026-03-15', '2026-06-25', '#A8D982', true, 4, 2);
*/