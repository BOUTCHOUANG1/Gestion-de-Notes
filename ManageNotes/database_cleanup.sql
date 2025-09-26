-- Connect to PostgreSQL as superuser
-- psql -U postgres -h localhost

-- Drop the existing database (this will remove all data and schema)
DROP DATABASE IF EXISTS "managerNotes";

-- Recreate the database
CREATE DATABASE "managerNotes"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Grant all privileges to postgres user
GRANT ALL PRIVILEGES ON DATABASE "managerNotes" TO postgres;

-- Connect to the new database
\c "managerNotes";

-- Ensure postgres user has all privileges on the schema
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;