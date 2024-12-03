create table if not exists CONFIG
(
    ID    BIGINT       not null,
    KEY   VARCHAR(255) not null
        constraint UX_CONFIG_KEY
            unique,
    VALUE VARCHAR(255),
    constraint PK_CONFIG
        primary key (ID)
);

