-- add column in Employee
alter table EMPLOYEE
    add column if not exists can_raise_rrf_on_behalf bool default false;

-- add column in RECRUITMENT_REQUISITION_FORM
alter table RECRUITMENT_REQUISITION_FORM
    add column if not exists created_at    timestamp,
    add column if not exists updated_at    timestamp,
    add column if not exists created_by_id bigint,
    add column if not exists updated_by_id bigint,
    add constraint FK_RRF_CREATED_BY_ID
        foreign key (created_by_id) references JHI_USER (ID),
    add constraint FK_RRF_UPDATED_BY_ID
        foreign key (updated_by_id) references JHI_USER (ID);

