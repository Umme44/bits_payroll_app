ALTER TABLE FESTIVAL_BONUS_DETAILS
    ADD COLUMN REMARKS VARCHAR(255),
    ADD COLUMN IS_HOLD BOOLEAN not null default false;

create table if not exists USER_FEEDBACK
(
    ID         BIGINT not null,
    RATING     INT    not null,
    SUGGESTION VARCHAR(255),
    USER_ID    BIGINT,
    constraint PK_USER_FEEDBACK
        primary key (ID),
    constraint FK_USER_FEEDBACK_USER_ID
        foreign key (USER_ID) references JHI_USER (ID)
);

create table if not exists HOLD_FB_DISBURSEMENT
(
    ID                       BIGINT not null,
    DISBURSED_AT             DATE   not null,
    REMARKS                  VARCHAR(255),
    DISBURSED_BY_ID          BIGINT not null,
    FESTIVAL_BONUS_DETAIL_ID BIGINT not null,
    constraint PK_HOLD_FB_DISBURSEMENT
        primary key (ID),
    constraint FK_HOLD_FB_DISBURSEMENT_DISBURSED_BY_ID
        foreign key (DISBURSED_BY_ID) references JHI_USER (ID),
    constraint FK_HOLD_FB_DISBURSEMENT_FESTIVAL_BONUS_DETAIL_ID
        foreign key (FESTIVAL_BONUS_DETAIL_ID) references FESTIVAL_BONUS_DETAILS (ID)
);
