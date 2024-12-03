create table if not exists EMPLOYEE_STATIC_FILE
(
    ID                  BIGINT not null,
    FILE_PATH           VARCHAR(255),
    EMPLOYEE_ID BIGINT,
    constraint PK_EMPLOYEE_STATIC_FILE
        primary key (ID),
    constraint FK_EMPLOYEE_STATIC_FILE_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID)
);
