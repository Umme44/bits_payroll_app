create table if not exists DEDUCTION_TYPE
(
    ID   BIGINT       not null,
    NAME VARCHAR(250) not null
        constraint UX_DEDUCTION_TYPE_NAME
            unique,
    constraint PK_DEDUCTION_TYPE
        primary key (ID)
);

create table if not exists SALARY_DEDUCTION
(
    ID                BIGINT not null,
    DEDUCTION_AMOUNT  DOUBLE precision not null,
    DEDUCTION_YEAR    INT    not null,
    DEDUCTION_MONTH   INT    not null,
    DEDUCTION_TYPE_ID BIGINT not null,
    EMPLOYEE_ID       BIGINT not null,
    constraint PK_SALARY_DEDUCTION
        primary key (ID),
    constraint FK_SALARY_DEDUCTION_DEDUCTION_TYPE_ID
        foreign key (DEDUCTION_TYPE_ID) references DEDUCTION_TYPE (ID),
    constraint FK_SALARY_DEDUCTION_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

ALTER TABLE SALARY_GENERATOR_MASTER
    ADD COLUMN  IS_SALARY_DEDUCTION_IMPORTED BOOLEAN DEFAULT FALSE,
    ADD COLUMN  IS_FINALIZED BOOLEAN DEFAULT FALSE;
