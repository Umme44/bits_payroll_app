ALTER TABLE recruitment_requisition_budget ADD COLUMN IF NOT EXISTS department_id bigint,
                                         add constraint fk_recruitment_requisition_budget_department_id
                                             foreign key (department_id) references DEPARTMENT (id);

