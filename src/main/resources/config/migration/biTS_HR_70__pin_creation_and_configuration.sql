-- Table Creation
create table EMPLOYEE_PIN
(
    ID                  BIGINT       not null,
    PIN                 VARCHAR(255)
        constraint UX_EMPLOYEE_PIN_PIN
            unique,
    FULL_NAME           VARCHAR(255) not null,
    EMPLOYEE_CATEGORY   VARCHAR(255) not null,
    EMPLOYEE_PIN_STATUS VARCHAR(255) not null,
    CREATED_AT          TIMESTAMP    not null,
    UPDATED_AT          TIMESTAMP,
    DEPARTMENT_ID       BIGINT       not null,
    DESIGNATION_ID      BIGINT       not null,
    UNIT_ID             BIGINT       not null,
    UPDATED_BY_ID       BIGINT,
    CREATED_BY_ID       BIGINT       not null,
    constraint PK_EMPLOYEE_PIN
        primary key (ID),
    constraint FK_EMPLOYEE_PIN_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYEE_PIN_DEPARTMENT_ID
        foreign key (DEPARTMENT_ID) references DEPARTMENT,
    constraint FK_EMPLOYEE_PIN_DESIGNATION_ID
        foreign key (DESIGNATION_ID) references DESIGNATION,
    constraint FK_EMPLOYEE_PIN_UNIT_ID
        foreign key (UNIT_ID) references UNIT,
    constraint FK_EMPLOYEE_PIN_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);


-- Table Creation
create table EMPLOYEE_PIN_CONFIGURATION
(
    ID                BIGINT       not null,
    EMPLOYEE_CATEGORY VARCHAR(255) not null,
    SEQUENCE_START    VARCHAR(255) not null
        constraint UX_EMPLOYEE_PIN_CONFIGURATION_SEQUENCE_START
            unique,
    SEQUENCE_END      VARCHAR(255) not null
        constraint UX_EMPLOYEE_PIN_CONFIGURATION_SEQUENCE_END
            unique,
    LAST_SEQUENCE     VARCHAR(255),
    HAS_FULL_FILLED   BOOLEAN,
    LAST_CREATED_PIN  VARCHAR (255),
    CREATED_AT        TIMESTAMP    not null,
    UPDATED_AT        TIMESTAMP,
    CREATED_BY_ID     BIGINT       not null,
    UPDATED_BY_ID     BIGINT,
    constraint PK_EMPLOYEE_PIN_CONFIGURATION
        primary key (ID),
    constraint FK_EMPLOYEE_PIN_CONFIGURATION_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_EMPLOYEE_PIN_CONFIGURATION_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);