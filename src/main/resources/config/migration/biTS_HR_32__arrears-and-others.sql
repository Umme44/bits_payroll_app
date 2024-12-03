create table if not exists ARREAR_SALARY_MASTER
(
    ID         BIGINT       not null,
    TITLE      VARCHAR(250) not null
        constraint UX_ARREAR_SALARY_MASTER_TITLE
            unique,
    IS_LOCKED  BOOLEAN      not null,
    IS_DELETED BOOLEAN      not null,
    constraint PK_ARREAR_SALARY_MASTER
        primary key (ID)
);

create table if not exists ARREAR_SALARY_ITEM
(
    ID                      BIGINT       not null,
    TITLE                   VARCHAR(255) not null,
    ARREAR_AMOUNT           DOUBLE PRECISION     not null,
    HAS_PF_ARREAR_DEDUCTION BOOLEAN,
    PF_ARREAR_DEDUCTION     DOUBLE PRECISION,
    IS_FESTIVAL_BONUS       BOOLEAN,
    IS_DELETED              BOOLEAN      not null,
    ARREAR_SALARY_MASTER_ID BIGINT       not null,
    EMPLOYEE_ID             BIGINT       not null,
    constraint PK_ARREAR_SALARY_ITEM
        primary key (ID),
    constraint FK_ARREAR_SALARY_ITEM_ARREAR_SALARY_MASTER_ID
        foreign key (ARREAR_SALARY_MASTER_ID) references ARREAR_SALARY_MASTER (ID),
    constraint FK_ARREAR_SALARY_ITEM_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

create table if not exists ARREAR_PAYMENT
(
    ID                      BIGINT       not null,
    PAYMENT_TYPE            VARCHAR(255) not null,
    DISBURSEMENT_DATE       DATE,
    SALARY_MONTH            VARCHAR(255),
    SALARY_YEAR             INT,
    APPROVAL_STATUS         VARCHAR(255),
    DISBURSEMENT_AMOUNT     DOUBLE PRECISION       not null,
    IS_DELETED              BOOLEAN      not null,
    ARREAR_PF               DOUBLE PRECISION       not null,
    TAX_DEDUCTION           DOUBLE PRECISION       not null,
    DEDUCT_TAX_UPON_PAYMENT BOOLEAN      not null,
    ARREAR_SALARY_ITEM_ID   BIGINT       not null,
    constraint PK_ARREAR_PAYMENT
        primary key (ID),
    constraint FK_ARREAR_PAYMENT_ARREAR_SALARY_ITEM_ID
        foreign key (ARREAR_SALARY_ITEM_ID) references ARREAR_SALARY_ITEM (ID)
);

create table if not exists HOLD_SALARY_DISBURSEMENT
(
    ID                 BIGINT not null,
    DATE               DATE   not null,
    USER_ID            BIGINT,
    EMPLOYEE_SALARY_ID BIGINT,
    constraint PK_HOLD_SALARY_DISBURSEMENT
        primary key (ID),
    constraint FK_HOLD_SALARY_DISBURSEMENT_EMPLOYEE_SALARY_ID
        foreign key (EMPLOYEE_SALARY_ID) references EMPLOYEE_SALARY (ID),
    constraint FK_HOLD_SALARY_DISBURSEMENT_USER_ID
        foreign key (USER_ID) references JHI_USER (ID)
);

create table if not exists FLEX_SCHEDULE
(
    ID             BIGINT                 not null,
    EFFECTIVE_DATE DATE                   not null,
    IN_TIME        TIMESTAMP default NULL not null,
    OUT_TIME       TIMESTAMP default NULL not null,
    EMPLOYEE_ID    BIGINT                 not null,
    CREATED_BY_ID  BIGINT                 not null,
    constraint PK_FLEX_SCHEDULE
        primary key (ID),
    constraint FK_FLEX_SCHEDULE_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_FLEX_SCHEDULE_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

