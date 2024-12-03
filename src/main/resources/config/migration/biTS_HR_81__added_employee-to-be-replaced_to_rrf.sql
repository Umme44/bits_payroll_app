ALTER TABLE recruitment_requisition_form ADD COLUMN IF NOT EXISTS employee_to_be_replaced_id bigint,
    add constraint fk_recruitment_requisition_form_employee_to_be_replaced_id
    foreign key (employee_to_be_replaced_id) references EMPLOYEE (id);

