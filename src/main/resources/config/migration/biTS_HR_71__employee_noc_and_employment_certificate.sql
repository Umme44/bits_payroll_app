-- Table Creation
create table EMPLOYEENOC
(
    ID                  BIGINT       not null,
    PASSPORT_NUMBER     VARCHAR(255) not null,
    LEAVE_START_DATE    DATE         not null,
    LEAVE_END_DATE      DATE         not null,
    COUNTRY_TO_VISIT    VARCHAR(255) not null,
    REFERENCE_NUMBER    VARCHAR(255)
        constraint UX_EMPLOYEENOC_REFERENCE_NUMBER
            unique,
    ISSUE_DATE          DATE,
    CREATED_AT          TIMESTAMP    not null,
    UPDATED_AT          TIMESTAMP,
    GENERATED_AT        TIMESTAMP,
    REASON              VARCHAR(255),
    PURPOSE_OF_NOC      VARCHAR(255) not null,
    CERTIFICATE_STATUS  VARCHAR(255) not null,
    IS_REQUIRED_FOR_VISA  BOOLEAN,
    EMPLOYEE_ID         BIGINT       not null,
    SIGNATORY_PERSON_ID BIGINT,
    CREATED_BY_ID       BIGINT       not null,
    UPDATED_BY_ID       BIGINT,
    GENERATED_BY_ID     BIGINT,
    constraint PK_EMPLOYEENOC
        primary key (ID),
    constraint FK_EMPLOYEENOC_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYEENOC_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE,
    constraint FK_EMPLOYEENOC_GENERATED_BY_ID
        foreign key (GENERATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYEENOC_SIGNATORY_PERSON_ID
        foreign key (SIGNATORY_PERSON_ID) references EMPLOYEE,
    constraint FK_EMPLOYEENOC_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);


-- Table creation
create table EMPLOYMENT_CERTIFICATE
(
    ID                  BIGINT       not null,
    CERTIFICATE_STATUS  VARCHAR(255) not null,
    REFERENCE_NUMBER    VARCHAR(255)
        constraint UX_EMPLOYMENT_CERTIFICATE_REFERENCE_NUMBER
            unique,
    ISSUE_DATE          DATE,
    REASON              VARCHAR(255),
    CREATED_AT          TIMESTAMP    not null,
    UPDATED_AT          TIMESTAMP,
    GENERATED_AT        TIMESTAMP,
    EMPLOYEE_ID         BIGINT       not null,
    SIGNATORY_PERSON_ID BIGINT,
    CREATED_BY_ID       BIGINT       not null,
    UPDATED_BY_ID       BIGINT,
    GENERATED_BY_ID     BIGINT,
    constraint PK_EMPLOYMENT_CERTIFICATE
        primary key (ID),
    constraint FK_EMPLOYMENT_CERTIFICATE_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYMENT_CERTIFICATE_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE,
    constraint FK_EMPLOYMENT_CERTIFICATE_GENERATED_BY_ID
        foreign key (GENERATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYMENT_CERTIFICATE_SIGNATORY_PERSON_ID
        foreign key (SIGNATORY_PERSON_ID) references EMPLOYEE,
    constraint FK_EMPLOYMENT_CERTIFICATE_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);