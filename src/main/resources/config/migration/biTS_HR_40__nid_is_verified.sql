alter table employee
    add column if not exists IS_NID_VERIFIED BOOLEAN default false;

alter table nominee
    add column if not exists IS_NID_VERIFIED          BOOLEAN default false,
    add column if not exists IS_GUARDIAN_NID_VERIFIED BOOLEAN default false;


alter table pf_nominee
    add column if not exists IS_NID_VERIFIED BOOLEAN default false,
    add column if not exists IS_GUARDIAN_NID_VERIFIED BOOLEAN default false;
