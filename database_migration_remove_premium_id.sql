-- Migration Script: Remove old premium_id column from passenger table
-- This script removes the old Premium entity relationship and keeps only InsurancePremium

-- Step 1: Check if the column exists
SELECT COLUMN_NAME, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'travelInsurance'
  AND TABLE_NAME = 'passenger'
  AND COLUMN_NAME = 'premium_id';

-- Step 2: Drop the foreign key constraint if it exists
-- Note: Replace 'fk_passenger_premium' with the actual constraint name if different
-- You can find the constraint name by running:
-- SHOW CREATE TABLE passenger;

-- Uncomment and run this if there's a foreign key constraint:
-- ALTER TABLE passenger DROP FOREIGN KEY fk_passenger_premium;

-- Step 3: Drop the premium_id column
ALTER TABLE passenger DROP COLUMN premium_id;

-- Step 4: Verify the column has been removed
DESCRIBE passenger;

-- Step 5: Verify the insurance_premium_id column exists
SELECT COLUMN_NAME, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'travelInsurance'
  AND TABLE_NAME = 'passenger'
  AND COLUMN_NAME = 'insurance_premium_id';

