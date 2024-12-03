create table if not exists SALARY_CERTIFICATE
(
    ID BIGINT not null,
    PURPOSE VARCHAR(250) not null,
    REMARKS VARCHAR(250),
    STATUS VARCHAR(255) not null,
    CREATED_AT DATE,
    UPDATED_AT DATE,
    SANCTION_AT DATE,
    EMPLOYEE_SALARY_ID BIGINT not null,
    CREATED_BY_ID BIGINT not null,
    UPDATED_BY_ID BIGINT,
    SANCTION_BY_ID BIGINT,
    constraint PK_SALARY_CERTIFICATE
        primary key (ID),
    constraint FK_SALARY_CERTIFICATE_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_SALARY_CERTIFICATE_EMPLOYEE_SALARY_ID
        foreign key (EMPLOYEE_SALARY_ID) references EMPLOYEE_SALARY (ID),
    constraint FK_SALARY_CERTIFICATE_SANCTION_BY_ID
        foreign key (SANCTION_BY_ID) references JHI_USER (ID),
    constraint FK_SALARY_CERTIFICATE_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);

