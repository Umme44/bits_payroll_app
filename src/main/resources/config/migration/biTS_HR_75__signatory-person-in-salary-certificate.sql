alter table salary_certificate
    ADD COLUMN IF NOT EXISTS signatory_person_id BIGINT,

    add constraint FK_SALARY_CERTIFICATE_SIGNATORY_PERSON_ID
    foreign key (SIGNATORY_PERSON_ID) references EMPLOYEE;

