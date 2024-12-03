ALTER TABLE PF_ACCOUNT
    ADD COLUMN if not exists     date_of_joining           DATE,
    ADD COLUMN if not exists     date_of_confirmation      DATE;
