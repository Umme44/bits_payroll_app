DROP TABLE FESTIVAL_BONUS_DETAILS;

DROP TABLE FESTIVAL_BONUS;


DROP TABLE FESTIVAL;

DROP TABLE FESTIVAL_BONUS_CONFIG;

create table if not exists FESTIVAL
(
    ID                      BIGINT       not null,
    TITLE                   VARCHAR(255) not null
        constraint UX_FESTIVAL_TITLE
            unique,
    FESTIVAL_NAME           VARCHAR(255),
    FESTIVAL_DATE           DATE,
    BONUS_DISBURSEMENT_DATE DATE         not null,
    RELIGION                VARCHAR(255) not null,
    IS_PRO_RATA             BOOLEAN      not null,
    constraint PK_FESTIVAL
        primary key (ID)
);

create table if not exists FESTIVAL_BONUS
(
    ID BIGINT not null,
    constraint PK_FESTIVAL_BONUS
        primary key (ID)
);

create table if not exists FESTIVAL_BONUS_DETAILS
(
    ID           BIGINT           not null,
    BONUS_AMOUNT DOUBLE precision not null,
    EMPLOYEE_ID  BIGINT           not null,
    FESTIVAL_ID  BIGINT           not null,
    constraint PK_FESTIVAL_BONUS_DETAILS
        primary key (ID)
);

create table if not exists FESTIVAL_BONUS_CONFIG
(
    ID                    BIGINT           not null,
    EMPLOYEE_CATEGORY     VARCHAR(255)     not null
        constraint UX_FESTIVAL_BONUS_CONFIG_EMPLOYEE_CATEGORY
            unique,
    PERCENTAGE_FROM_GROSS DOUBLE PRECISION not null,
    constraint PK_FESTIVAL_BONUS_CONFIG
        primary key (ID)
);
