alter table employee alter column updated_at type timestamp;
update employee set updated_at = '1971-03-26 14:30:00' where updated_at is null;
