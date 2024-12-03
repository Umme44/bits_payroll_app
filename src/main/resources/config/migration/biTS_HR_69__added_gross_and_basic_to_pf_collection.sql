ALTER TABLE PF_COLLECTION
    ADD COLUMN if not exists gross DOUBLE PRECISION default 0,
    ADD COLUMN if not exists basic DOUBLE PRECISION default 0;
