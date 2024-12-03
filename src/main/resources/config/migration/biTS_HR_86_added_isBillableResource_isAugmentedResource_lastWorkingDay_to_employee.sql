ALTER TABLE employee ADD COLUMN IF NOT EXISTS is_billable_resource boolean default false;
ALTER TABLE employee ADD COLUMN IF NOT EXISTS is_augmented_resource boolean default false;
ALTER TABLE employee ADD COLUMN IF NOT EXISTS last_working_day TIMESTAMP;
