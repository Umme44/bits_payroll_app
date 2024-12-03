-- work from home application table creation script
create table WORK_FROM_HOME_APPLICATION
(
    ID               BIGINT       not null,
    START_DATE       DATE         not null,
    END_DATE         DATE         not null,
    REASON           VARCHAR(250) not null,
    DURATION         INT,
    STATUS           VARCHAR(255) not null,
    APPLIED_AT       DATE,
    UPDATED_AT       TIMESTAMP default NULL,
    CREATED_AT       TIMESTAMP default NULL,
    SANCTIONED_AT    TIMESTAMP default NULL,
    APPLIED_BY_ID    BIGINT,
    CREATED_BY_ID    BIGINT,
    UPDATED_BY_ID    BIGINT,
    SANCTIONED_BY_ID BIGINT,
    EMPLOYEE_ID      BIGINT       not null,
    constraint PK_WORK_FROM_HOME_APPLICATION
        primary key (ID),
    constraint FK_WORK_FROM_HOME_APPLICATION_APPLIED_BY_ID
        foreign key (APPLIED_BY_ID) references JHI_USER,
    constraint FK_WORK_FROM_HOME_APPLICATION_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_WORK_FROM_HOME_APPLICATION_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE,
    constraint FK_WORK_FROM_HOME_APPLICATION_SANCTIONED_BY_ID
        foreign key (SANCTIONED_BY_ID) references JHI_USER,
    constraint FK_WORK_FROM_HOME_APPLICATION_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);

