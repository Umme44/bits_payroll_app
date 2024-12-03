create table if not exists MOVEMENT_ENTRY
(
    ID BIGINT not null,
    START_DATE DATE not null,
    START_TIME TIMESTAMP default NULL not null,
    START_NOTE VARCHAR(250) not null,
    END_DATE DATE not null,
    END_TIME TIMESTAMP default NULL not null,
    END_NOTE VARCHAR(250) not null,
    TYPE VARCHAR(255) not null,
    STATUS VARCHAR(255) not null,
    CREATED_AT DATE not null,
    UPDATED_AT DATE,
    SANCTION_AT DATE,
    EMPLOYEE_ID BIGINT not null,
    CREATED_BY_ID BIGINT not null,
    UPDATED_BY_ID BIGINT,
    SANCTION_BY_ID BIGINT,
    constraint PK_MOVEMENT_ENTRY
        primary key (ID),
    constraint FK_MOVEMENT_ENTRY_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER (ID),
    constraint FK_MOVEMENT_ENTRY_EMPLOYEE_ID
        foreign key (EMPLOYEE_ID) references EMPLOYEE (ID),
    constraint FK_MOVEMENT_ENTRY_SANCTION_BY_ID
        foreign key (SANCTION_BY_ID) references JHI_USER (ID),
    constraint FK_MOVEMENT_ENTRY_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER (ID)
);

