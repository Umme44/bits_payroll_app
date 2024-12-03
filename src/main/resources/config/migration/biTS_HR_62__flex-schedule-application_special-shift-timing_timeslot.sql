-- new table flex_schedule_application
create table if not exists FLEX_SCHEDULE_APPLICATION
(
    ID               BIGINT                 not null,
    EFFECTIVE_FROM   DATE                   not null,
    EFFECTIVE_TO     DATE                   not null,
    REASON           text,
    STATUS           VARCHAR(255)           not null,
    SANCTIONED_AT    TIMESTAMP default NULL,
    APPLIED_AT       DATE                   not null,
    CREATED_AT       TIMESTAMP default NULL not null,
    UPDATED_AT       TIMESTAMP default NULL,
    REQUESTER_ID     BIGINT                 not null,
    SANCTIONED_BY_ID BIGINT,
    APPLIED_BY_ID    BIGINT,
    CREATED_BY_ID    BIGINT                 not null,
    UPDATED_BY_ID    BIGINT,
    TIME_SLOT_ID     BIGINT                 not null,
    constraint PK_FLEX_SCHEDULE_APPLICATION
        primary key (ID),
    constraint FK_FLEX_SCHEDULE_APPLICATION_APPLIED_BY_ID
        foreign key (APPLIED_BY_ID) references JHI_USER,
    constraint FK_FLEX_SCHEDULE_APPLICATION_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_FLEX_SCHEDULE_APPLICATION_REQUESTER_ID
        foreign key (REQUESTER_ID) references EMPLOYEE,
    constraint FK_FLEX_SCHEDULE_APPLICATION_SANCTIONED_BY_ID
        foreign key (SANCTIONED_BY_ID) references JHI_USER,
    constraint FK_FLEX_SCHEDULE_APPLICATION_TIME_SLOT_ID
        foreign key (TIME_SLOT_ID) references TIME_SLOT,
    constraint FK_FLEX_SCHEDULE_APPLICATION_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);


-- new table special_shift_timing
create table if not exists SPECIAL_SHIFT_TIMING
(
    ID                     BIGINT  not null,
    START_DATE             DATE    not null,
    END_DATE               DATE    not null,
    OVERRIDE_ROASTER       BOOLEAN not null,
    OVERRIDE_FLEX_SCHEDULE BOOLEAN not null,
    REMARKS                VARCHAR(255),
    CREATED_AT             TIMESTAMP default NULL,
    UPDATED_AT             TIMESTAMP default NULL,
    REASON                 VARCHAR(250),
    TIME_SLOT_ID           BIGINT  not null,
    CREATED_BY_ID          BIGINT,
    UPDATED_BY_ID          BIGINT,
    constraint PK_SPECIAL_SHIFT_TIMING
        primary key (ID),
    constraint FK_SPECIAL_SHIFT_TIMING_CREATED_BY_ID
        foreign key (CREATED_BY_ID) references JHI_USER,
    constraint FK_SPECIAL_SHIFT_TIMING_TIME_SLOT_ID
        foreign key (TIME_SLOT_ID) references TIME_SLOT,
    constraint FK_SPECIAL_SHIFT_TIMING_UPDATED_BY_ID
        foreign key (UPDATED_BY_ID) references JHI_USER
);

-- TIME_SLOT new column [is_applicable_by_employee, isDefaultShift, code, week_ends]
alter table if exists TIME_SLOT
    add column if not exists IS_APPLICABLE_BY_EMPLOYEE boolean default false,
    add column if not exists IS_DEFAULT_SHIFT boolean default false,
    add column if not exists CODE VARCHAR(50)
                    constraint UX_TIME_SLOT_CODE
                    unique,
    add column if not exists WEEK_ENDS text;
