-- add column in Employee
ALTER TABLE EMPLOYEE
    ADD COLUMN IF NOT EXISTS is_eligible_for_automated_attendance BOOLEAN default false;

