-- Add audit columns to departments table to match AbstractEntity
ALTER TABLE departments 
ADD COLUMN IF NOT EXISTS creation_date timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS last_modified_date timestamp(6) with time zone;

-- Update existing records to have creation_date
UPDATE departments SET creation_date = CURRENT_TIMESTAMP WHERE creation_date IS NULL;

-- Make creation_date NOT NULL
ALTER TABLE departments ALTER COLUMN creation_date SET NOT NULL;