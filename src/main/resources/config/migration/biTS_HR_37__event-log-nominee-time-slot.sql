-- event_log table creation
create table if not exists EVENT_LOG
(
    ID              BIGINT                 not null,
    TITLE           VARCHAR(255),
    REQUEST_METHOD  VARCHAR(255),
    PERFORMED_AT    TIMESTAMP default NULL not null,
    DATA            TEXT,
    ENTITY_NAME     VARCHAR(255),
    PERFORMED_BY_ID BIGINT                 not null,
    constraint PK_EVENT_LOG
        primary key (ID),
    constraint FK_EVENT_LOG_PERFORMED_BY_ID
        foreign key (PERFORMED_BY_ID) references JHI_USER (ID)
);


-- nominee table-creation
create table if not exists NOMINEE
(
    ID                         BIGINT           not null,
    NOMINEE_NAME               VARCHAR(255)     not null,
    PRESENT_ADDRESS            VARCHAR(255)     not null,
    RELATIONSHIP_WITH_EMPLOYEE VARCHAR(255),
    DATE_OF_BIRTH              DATE,
    AGE                        INT,
    SHARE_PERCENTAGE           DOUBLE precision not null,
    IMAGE_PATH                 VARCHAR(255),
    GUARDIAN_NAME              VARCHAR(255),
    GUARDIAN_FATHER_NAME       VARCHAR(255),
    GUARDIAN_SPOUSE_NAME       VARCHAR(255),
    GUARDIAN_DATE_OF_BIRTH     DATE,
    GUARDIAN_PRESENT_ADDRESS   VARCHAR(255),
    GUARDIAN_DOCUMENT_NAME     VARCHAR(255),
    GUARDIAN_RELATIONSHIP_WITH VARCHAR(255),
    GUARDIAN_IMAGE_PATH        VARCHAR(255),
    IS_LOCKED                  BOOLEAN,
    NOMINATION_DATE            DATE,
    PERMANENT_ADDRESS          VARCHAR(255)     not null,
    GUARDIAN_PERMANENT_ADDRESS VARCHAR(255),
    NOMINEE_TYPE               VARCHAR(255),
    IDENTITY_TYPE              VARCHAR(255),
    DOCUMENT_NAME              VARCHAR(255),
    ID_NUMBER                  VARCHAR(255),
    GUARDIAN_IDENTITY_TYPE     VARCHAR(255),
    GUARDIAN_ID_NUMBER         VARCHAR(255),
    EMPLOYEE_ID                BIGINT,
    APPROVED_BY_ID             BIGINT,
    WITNESS_ID                 BIGINT,
    MEMBER_ID                  BIGINT,
    constraint PK_NOMINEE
        primary key (ID),
    constraint FK_NOMINEE_APPROVED_BY_ID
        foreign key (APPROVED_BY_ID) references EMPLOYEE (ID),
    constraint FK_NOMINEE_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID),
    constraint FK_NOMINEE_MEMBER_ID
        foreign key (MEMBER_ID) references EMPLOYEE (ID),
    constraint FK_NOMINEE_WITNESS_ID
        foreign key (WITNESS_ID) references EMPLOYEE (ID)
);


-- time_slot table creation
create table if not exists time_slot
(
    id       bigint       not null
        constraint pk_time_slot
            primary key,
    title    varchar(250) not null
        constraint ux_time_slot_title
            unique,
    in_time  timestamp    not null,
    out_time timestamp    not null
);



