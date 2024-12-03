create table ARREAR_SALARY
(
    ID BIGINT not null,
    MONTH VARCHAR(255) not null,
    YEAR INT not null,
    AMOUNT DOUBLE PRECISION not null,
    EMPLOYEE_ID BIGINT not null,
    constraint PK_ARREAR_SALARY
        primary key (ID),
    constraint FK_ARREAR_SALARY_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

