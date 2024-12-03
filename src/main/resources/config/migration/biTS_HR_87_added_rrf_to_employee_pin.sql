ALTER TABLE EMPLOYEE_PIN ADD COLUMN IF NOT EXISTS
    recruitment_requisition_form_id bigint,
    add constraint fk_recruitment_requisition_form_id
    foreign key (recruitment_requisition_form_id) references RECRUITMENT_REQUISITION_FORM (id);
