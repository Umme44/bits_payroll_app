-- add new column visibility
ALTER TABLE SALARY_GENERATOR_MASTER
    ADD COLUMN if not exists visibility VARCHAR(255) default 'NOT_VISIBLE_TO_EMPLOYEE';

-- add new column basic, gross
ALTER TABLE FESTIVAL_BONUS_DETAILS
    ADD COLUMN if not exists basic double precision DEFAULT 0,
    ADD COLUMN if not exists gross double precision DEFAULT 0;
