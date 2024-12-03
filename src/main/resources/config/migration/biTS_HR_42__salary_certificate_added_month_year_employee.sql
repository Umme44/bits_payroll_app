drop table if exists salary_certificate;

create table salary_certificate
(
    id             bigint       not null
        primary key,
    purpose        varchar(250) not null,
    remarks        varchar(250),
    status         varchar(255) not null,
    created_at     date,
    updated_at     date,
    sanction_at    date,
    month          varchar(255) not null,
    year           integer      not null,
    created_by_id  bigint       not null
        constraint fk_salary_certificate_created_by_id
            references jhi_user,
    updated_by_id  bigint
        constraint fk_salary_certificate_updated_by_id
            references jhi_user,
    sanction_by_id bigint
        constraint fk_salary_certificate_sanction_by_id
            references jhi_user,
    employee_id    bigint       not null
        constraint fk_salary_certificate_employee_id
            references employee
);
