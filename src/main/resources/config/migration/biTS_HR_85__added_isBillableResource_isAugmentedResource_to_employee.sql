ALTER TABLE EMPLOYEE
    ADD COLUMN IF NOT EXISTS is_billable_resource boolean default false,
    ADD COLUMN IF NOT EXISTS is_augmented_resource boolean default false
