ALTER TABLE EMPLOYEE ADD COLUMN IF NOT EXISTS is_currently_resigned BOOLEAN DEFAULT false;


ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS preferred_skill_type VARCHAR(255);
ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS recruitment_nature_enum VARCHAR(255);
ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS recommendation_date_05 date;
ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS special_requirement VARCHAR(255);
ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS recommended_by05_id bigint,
    add constraint fk_recruitment_requisition_form_recommended_by05_id
        foreign key (recommended_by05_id) references EMPLOYEE;


CREATE TABLE IF NOT EXISTS recruitment_requisition_budget
(
    id                 bigint not null,
    year               bigint not null,
    remaining_budget   bigint,
    budget             bigint,
    remaining_manpower bigint,
    employee_id        bigint,
    CONSTRAINT pk_recruitment_requisition_budget
        PRIMARY KEY (id),
    constraint pfk_recruitment_requisition_budget
        foreign key (employee_id) references employee (id)
);

create table if not exists employee_document
(
    id bigint not null
        constraint employee_documents_pkey
            primary key,
    pin varchar(255) not null,
    file_name varchar(255) not null,
    file_path varchar(255) not null,
    file_extension varchar(255) not null,
    has_employee_visibility boolean,
    remarks varchar(255),
    created_by varchar(255) ,
    created_at timestamp,
    updated_by varchar(255) ,
    updated_at timestamp
);
