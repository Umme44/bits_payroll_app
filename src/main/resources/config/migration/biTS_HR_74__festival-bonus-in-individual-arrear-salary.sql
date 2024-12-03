alter table individual_arrear_salary
    ADD COLUMN if not exists festival_bonus DOUBLE PRECISION default 0;
