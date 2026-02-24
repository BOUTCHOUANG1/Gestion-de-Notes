-- Initial baseline to allow Flyway validation without altering existing schema
-- If the schema is already applied, Flyway baseline-on-migrate=true will mark it as baseline.
-- Add minimal no-op statement to satisfy Flyway.
SELECT 1;
