create table if not exists PF_ARREAR
(
    ID          BIGINT       not null,
    MONTH       VARCHAR(255) not null,
    YEAR        INT          not null,
    AMOUNT      DOUBLE PRECISION       not null,
    REMARKS     VARCHAR(250) not null,
    EMPLOYEE_ID BIGINT,
    constraint PK_PF_ARREAR
        primary key (ID),
    constraint FK_PF_ARREAR_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

