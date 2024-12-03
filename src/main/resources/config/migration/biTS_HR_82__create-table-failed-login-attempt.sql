create table if not exists failed_login_attempt
(
    id bigint not null
        constraint failed_login_attempt_pkey
            primary key,
    user_name varchar(255) not null
        constraint ux_failed_login_attempt__user_name
            unique,
    continuous_failed_attempts integer not null,
    last_failed_attempt timestamp
);
