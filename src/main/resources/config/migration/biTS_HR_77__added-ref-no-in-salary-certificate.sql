ALTER TABLE SALARY_CERTIFICATE
    ADD COLUMN if not exists reference_number VARCHAR(255) default NULL;
