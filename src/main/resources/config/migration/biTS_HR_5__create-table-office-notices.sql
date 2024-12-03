create table if not exists OFFICE_NOTICES
(
    ID BIGINT not null,
    TITLE VARCHAR(250) not null,
    DESCRIPTION text,
    STATUS VARCHAR(255),
    PUBLISH_FORM DATE,
    PUBLISH_TO DATE,
    CREATED_AT DATE,
    UPDATED_AT DATE,
    CREATED_BY VARCHAR(255),
    UPDATED_BY VARCHAR(255),
    constraint PK_OFFICE_NOTICES
        primary key (ID)
);
