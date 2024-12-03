ALTER TABLE FINAL_SETTLEMENT
    ADD COLUMN IS_FINALIZED                          BOOLEAN default false,
    ADD COLUMN SALARY_NUM_OF_MONTH                   INT default 1,
    ADD COLUMN REMARKS                               VARCHAR(255);
