-- new authority for prf
INSERT INTO jhi_authority (name)
VALUES ('ROLE_PROCUREMENT_OFFICER');

-- update table :: Department (departmentHead)
alter table department
    add column department_head_id bigint
        constraint fk_department_head_id references employee;

-- create table :: UnitOfMeasurement

create table if not exists unit_of_measurement
(
    id            bigint       not null
        primary key,
    created_at    timestamp    not null,
    name          varchar(255) not null
        constraint uk_name
            unique,
    remarks       varchar(255),
    updated_at    timestamp,
    created_by_id bigint       not null
        constraint fk_created_by_id
            references jhi_user,
    updated_by_id bigint
        constraint fk_updated_by_id
            references jhi_user
);

-- create table :: ItemInformation

create table if not exists item_information
(
    id                     bigint       not null
        primary key,
    code                   varchar(255) not null
        constraint uk_code
            unique,
    created_at             timestamp    not null,
    name                   varchar(255) not null,
    specification          text         not null,
    updated_at             timestamp,
    created_by_id          bigint       not null
        constraint fk_created_by_id
            references jhi_user,
    department_id          bigint       not null
        constraint fk_department_id
            references department,
    unit_of_measurement_id bigint       not null
        constraint fk_unit_of_measurement_id
            references unit_of_measurement,
    updated_by_id          bigint
        constraint fk_updated_by_id
            references jhi_user
);


-- create table :: ProcReqMaster

create table proc_req_master
(
    id                        bigint       not null
        primary key,
    created_at                timestamp    not null,
    closed_at                 timestamp,
    expected_received_date    date,
    is_cto_approval_required  boolean,
    next_recommendation_order integer,
    reasoning                 text,
    recommendation_at_01      timestamp,
    recommendation_at_02      timestamp,
    recommendation_at_03      timestamp,
    recommendation_at_04      timestamp,
    recommendation_at_05      timestamp,
    rejected_date             date,
    rejection_reason          text,
    requested_date            date         not null,
    requisition_no            varchar(255) not null
        constraint uk_requisition_no
            unique,
    requisition_status        varchar(255) not null,
    total_approximate_price   double precision,
    updated_at                timestamp,
    created_by_id             bigint       not null
        constraint fk_created_by_id
            references jhi_user,
    closed_by_id              bigint
        constraint fk_closed_by_id
            references employee,
    department_id             bigint       not null
        constraint fk_department_id
            references department,
    next_approval_from_id     bigint
        constraint fk_next_approval_from_id
            references employee,
    recommended_by01_id       bigint
        constraint fk_recommended_by01_id
            references employee,
    recommended_by02_id       bigint
        constraint fk_recommended_by02_id
            references employee,
    recommended_by03_id       bigint
        constraint fk_recommended_by03_id
            references employee,
    recommended_by04_id       bigint
        constraint fk_recommended_by04_id
            references employee,
    recommended_by05_id       bigint
        constraint fk_recommended_by05_id
            references employee,
    rejected_by_id            bigint
        constraint fk_rejected_by_id
            references employee,
    requested_by_id           bigint       not null
        constraint fk_requested_by_id
            references employee,
    updated_by_id             bigint
        constraint fk_updated_by_id
            references jhi_user

);

-- create table :: ProcReq

create table proc_req
(
    id                  bigint           not null
        primary key,
    quantity            double precision not null,
    reference_file_path text,
    item_information_id bigint           not null
        constraint fk_item_information_id
            references item_information,
    proc_req_master_id  bigint           not null
        constraint fk_proc_req_master_id
            references proc_req_master
);
