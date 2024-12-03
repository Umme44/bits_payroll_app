-- new property total onboard in rrf
ALTER TABLE RECRUITMENT_REQUISITION_FORM
    ADD COLUMN IF NOT EXISTS  total_onboard  INT default 0;
