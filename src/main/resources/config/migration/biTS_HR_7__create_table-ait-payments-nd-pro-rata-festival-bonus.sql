create table if not exists AIT_PAYMENT
(
    ID BIGINT not null,
    DATE DATE,
    AMOUNT DOUBLE PRECISION,
    DESCRIPTION VARCHAR(255),
    EMPLOYEE_ID BIGINT,
    constraint PK_AIT_PAYMENT
        primary key (ID),
    constraint FK_AIT_PAYMENT_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

create table if not exists PRO_RATA_FESTIVAL_BONUS
(
    ID          BIGINT not null,
    DATE        DATE,
    AMOUNT      DOUBLE PRECISION,
    DESCRIPTION VARCHAR(255),
    EMPLOYEE_ID BIGINT,
    constraint PK_PRO_RATA_FESTIVAL_BONUS
        primary key (ID),
    constraint FK_PRO_RATA_FESTIVAL_BONUS_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);

