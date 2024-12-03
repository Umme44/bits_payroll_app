create table if not exists OFFER
(
    ID          BIGINT       not null,
    TITLE       VARCHAR(255),
    DESCRIPTION VARCHAR(250) not null,
    IMAGE_PATH  VARCHAR(255),
    CREATED_AT  DATE,
    constraint PK_OFFER
        primary key (ID)
);

