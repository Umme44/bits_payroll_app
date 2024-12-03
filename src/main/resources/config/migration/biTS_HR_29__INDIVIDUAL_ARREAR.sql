create table if not exists INDIVIDUAL_ARREAR_SALARY
(
    ID                   BIGINT not null,
    EFFECTIVE_DATE       DATE,
    EXISTING_BAND        VARCHAR(255),
    NEW_BAND             VARCHAR(255),
    EXISTING_GROSS       DOUBLE PRECISION,
    NEW_GROSS            DOUBLE PRECISION,
    INCREMENT            DOUBLE PRECISION,
    ARREAR_SALARY        DOUBLE PRECISION,
    ARREAR_PF_DEDUCTION  DOUBLE PRECISION,
    TAX_DEDUCTION        DOUBLE PRECISION,
    NET_PAY              DOUBLE PRECISION,
    PF_CONTRIBUTION      DOUBLE PRECISION,
    TITLE                VARCHAR(255),
    TITLE_EFFECTIVE_FROM VARCHAR(255),
    ARREAR_REMARKS       VARCHAR(255),
    EMPLOYEE_ID          BIGINT,
    constraint PK_INDIVIDUAL_ARREAR_SALARY
        primary key (ID),
    constraint FK_INDIVIDUAL_ARREAR_SALARY_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

