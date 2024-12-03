-- Vehicle Module

-- Vehicle Table Created
create table VEHICLE
(
    ID                       BIGINT       not null,
    MODEL_NAME               VARCHAR(255) not null,
    BRAND                    VARCHAR(255) not null,
    CHASSIS_NUMBER           VARCHAR(255) not null,
    REGISTRATION_NUMBER      VARCHAR(255) not null,
    STATUS                   VARCHAR(255) not null,
    CAPACITY                 INT          not null,
    HAS_AIR_CONDITION_SYSTEM BOOLEAN      not null,
    REMARKS                  VARCHAR(255),
    CREATED_AT               TIMESTAMP default NULL,
    UPDATED_AT               TIMESTAMP default NULL,
    APPROVED_AT              TIMESTAMP default NULL,
    CREATED_BY_ID            BIGINT,
    UPDATED_BY_ID            BIGINT,
    APPROVED_BY_ID           BIGINT,
    constraint PK_VEHICLE
        primary key (ID),
    constraint FK_VEHICLE_APPROVED_BY_ID
        foreign key (APPROVED_BY_ID) references JHI_USER (ID),
    constraint FK_VEHICLE_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_VEHICLE_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);

-- Vehicle Requisition Table Created
create table VEHICLE_REQUISITION
(
    ID                         BIGINT       not null,
    REQUESTED_DATE             DATE         not null,
    START_HOUR                 INT          not null,
    START_MINUTE               INT          not null,
    END_HOUR                   INT          not null,
    END_MINUTE                 INT          not null,
    PURPOSE                    VARCHAR(255) not null,
    OTHER_PASSENGERS_NAME      VARCHAR(255),
    TOTAL_NUMBER_OF_PASSENGERS BIGINT       not null,
    PICKUP_LOCATION            VARCHAR(255) not null,
    DROP_LOCATION              VARCHAR(255) not null,
    STATUS                     VARCHAR(255) not null,
    REMARKS                    VARCHAR(255),
    CREATED_AT                 TIMESTAMP default NULL,
    UPDATED_AT                 TIMESTAMP default NULL,
    SANCTION_AT                TIMESTAMP default NULL,
    CREATED_BY_ID              BIGINT,
    UPDATED_BY_ID              BIGINT,
    APPROVED_BY_ID             BIGINT,
    REQUESTER_ID               BIGINT,
    VEHICLE_ID                 BIGINT,
    constraint PK_VEHICLE_REQUISITION
        primary key (ID),
    constraint FK_VEHICLE_REQUISITION_APPROVED_BY_ID
        foreign key (APPROVED_BY_ID) references JHI_USER (ID),
    constraint FK_VEHICLE_REQUISITION_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_VEHICLE_REQUISITION_REQUESTER_ID
        foreign key (REQUESTER_ID) references EMPLOYEE (ID),
    constraint FK_VEHICLE_REQUISITION_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID),
    constraint FK_VEHICLE_REQUISITION_VEHICLE_ID
        foreign key (VEHICLE_ID) references VEHICLE (ID)
);



-- Insurance Module


-- Insurance Relation Created
create table INSURANCE_RELATION
(
    ID            BIGINT       not null,
    RELATION_NAME VARCHAR(200)
        constraint UX_INSURANCE_RELATION_RELATION_NAME
            unique,
    RELATION      VARCHAR(255) not null,
    constraint PK_INSURANCE_RELATION
        primary key (ID)
);


-- Insurance Registration Table Created
create table INSURANCE_REGISTRATION
(
    ID                    BIGINT       not null,
    NAME                  VARCHAR(250),
    DATE_OF_BIRTH         DATE         not null,
    RELATION_TYPE         VARCHAR(255) not null,
    PHOTO                 TEXT,
    STATUS                VARCHAR(255) not null,
    APPROVED_AT           TIMESTAMP default NULL,
    CREATED_AT            TIMESTAMP default NULL,
    UPDATED_AT            TIMESTAMP default NULL,
    INSURANCE_ID          VARCHAR(255),
    REASON                VARCHAR(255),
    EMPLOYEE_ID           BIGINT       not null,
    APPROVED_BY_ID        BIGINT,
    CREATED_BY_ID         BIGINT,
    UPDATED_BY_ID         BIGINT,
    INSURANCE_RELATION_ID BIGINT       not null,
    constraint PK_INSURANCE_REGISTRATION
        primary key (ID),
    constraint FK_INSURANCE_REGISTRATION_APPROVED_BY_ID
        foreign key (APPROVED_BY_ID) references JHI_USER (ID),
    constraint FK_INSURANCE_REGISTRATION_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_INSURANCE_REGISTRATION_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID),
    constraint FK_INSURANCE_REGISTRATION_INSURANCE_RELATION_ID
        foreign key (INSURANCE_RELATION_ID) references INSURANCE_RELATION (ID),
    constraint FK_INSURANCE_REGISTRATION_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);



-- Insurance Configuration Table Created
create table INSURANCE_CONFIGURATION
(
    ID                             BIGINT not null,
    MAX_TOTAL_CLAIM_LIMIT_PER_YEAR DOUBLE precision not null,
    MAX_ALLOWED_CHILD_AGE          DOUBLE precision,
    PREFERRED_HOSPITAL_URL         TEXT,
    constraint PK_INSURANCE_CONFIGURATION
        primary key (ID)
);


-- Insurance Claim Table Created
create table INSURANCE_CLAIM
(
    ID                            BIGINT       not null,
    DATE_TO_PRIOR_INTIMATION      DATE         not null,
    DATE_OF_ADMISSION             DATE         not null,
    DATE_OF_DISCHARGE             DATE         not null,
    NAME_OF_HOSPITAL              VARCHAR(250) not null,
    AREA                          VARCHAR(255) not null,
    MEDICAL_INVESTIGATION_EXPENSE DOUBLE precision,
    SURGICAL_CHARGE               DOUBLE precision,
    MEDICINE_AND_DRUGES           DOUBLE precision,
    ANCILLARY_SERVICES            DOUBLE precision,
    CONSULTANT_FEE                DOUBLE precision,
    OTHERS                        DOUBLE precision,
    ACCEPTED_AMOUNT               DOUBLE precision,
    STATUS                        VARCHAR(255) not null,
    APPROVED_AT                   TIMESTAMP default NULL,
    CREATED_AT                    TIMESTAMP default NULL,
    UPDATED_AT                    TIMESTAMP default NULL,
    CLAIM_TYPE                    VARCHAR(255) not null,
    HOSPITAL_ACCOMMODATION_CHARGE DOUBLE precision,
    REASON                        VARCHAR(255),
    INSURANCE_REGISTRATION_ID     BIGINT       not null,
    APPROVED_BY_ID                BIGINT,
    CREATED_BY_ID                 BIGINT,
    UPDATED_BY_ID                 BIGINT,
    constraint PK_INSURANCE_CLAIM
        primary key (ID),
    constraint FK_INSURANCE_CLAIM_APPROVED_BY_ID
        foreign key (APPROVED_BY_ID) references JHI_USER (ID),
    constraint FK_INSURANCE_CLAIM_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_INSURANCE_CLAIM_INSURANCE_REGISTRATION_ID
        foreign key (INSURANCE_REGISTRATION_ID) references INSURANCE_REGISTRATION (ID),
    constraint FK_INSURANCE_CLAIM_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);



-- auto-generated definition
create table TAX_DOCUMENT
(
    ID               BIGINT       not null,
    DOCUMENT_NAME    VARCHAR(250) not null,
    STATUS           VARCHAR(255) not null,
    FILE_PATH        TEXT,
    CREATED_AT       TIMESTAMP default NULL,
    UPDATED_AT       TIMESTAMP default NULL,
    SANCTIONED_AT    TIMESTAMP default NULL,
    FISCAL_YEAR_ID   BIGINT       not null,
    EMPLOYEE_ID      BIGINT       not null,
    CREATED_BY_ID    BIGINT,
    UPDATED_BY_ID    BIGINT,
    SANCTIONED_BY_ID BIGINT,
    constraint PK_TAX_DOCUMENT
        primary key (ID),
    constraint FK_TAX_DOCUMENT_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_TAX_DOCUMENT_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID),
    constraint FK_TAX_DOCUMENT_FISCAL_YEAR_ID
        foreign key (FISCAL_YEAR_ID) references AIT_CONFIG (ID),
    constraint FK_TAX_DOCUMENT_SANCTIONED_BY_ID
        foreign key (SANCTIONED_BY_ID) references JHI_USER (ID),
    constraint FK_TAX_DOCUMENT_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);



ALTER TABLE Holidays ADD COLUMN
    IS_MOON_DEPENDENT BOOLEAN DEFAULT FALSE;
