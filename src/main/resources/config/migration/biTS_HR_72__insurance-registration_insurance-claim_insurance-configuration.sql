
-- delete & add column in InsuranceRegistration
ALTER TABLE IF EXISTS insurance_registration

    DROP COLUMN IF EXISTS insurance_relation_id,
    DROP COLUMN IF EXISTS relation_type,
    DROP COLUMN IF EXISTS reason,
    DROP COLUMN IF EXISTS status,
    ADD COLUMN IF NOT EXISTS insurance_relation VARCHAR(255)     DEFAULT 'SELF',
    ADD COLUMN IF NOT EXISTS insurance_status   VARCHAR(255)     DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS unapproval_reason  VARCHAR(255),
    ADD COLUMN IF NOT EXISTS available_balance  DOUBLE PRECISION DEFAULT 0;

-- delete & add column in InsuranceClaim
ALTER TABLE IF EXISTS insurance_claim
    DROP COLUMN IF EXISTS  date_to_prior_intimation,
    DROP COLUMN IF EXISTS  date_of_admission,
    DROP COLUMN IF EXISTS  date_of_discharge,
    DROP COLUMN IF EXISTS  name_of_hospital,
    DROP COLUMN IF EXISTS  area,
    DROP COLUMN IF EXISTS  medical_investigation_expense,
    DROP COLUMN IF EXISTS  hospital_accommodation_charge,
    DROP COLUMN IF EXISTS  surgical_charge,
    DROP COLUMN IF EXISTS  medicine_and_druges,
    DROP COLUMN IF EXISTS  ancillary_services,
    DROP COLUMN IF EXISTS  consultant_fee,
    DROP COLUMN IF EXISTS  others,
    DROP COLUMN IF EXISTS  status,
    DROP COLUMN IF EXISTS  claim_type,
    DROP COLUMN IF EXISTS  reason,
    DROP COLUMN IF EXISTS  approved_by_id,
    DROP COLUMN IF EXISTS  approved_at,
    DROP COLUMN IF EXISTS  accepted_amount,

    ADD COLUMN IF NOT EXISTS regret_reason VARCHAR(255),
    ADD COLUMN IF NOT EXISTS regret_date DATE,
    ADD COLUMN IF NOT EXISTS settlement_date DATE,
    ADD COLUMN IF NOT EXISTS payment_date DATE,
    ADD COLUMN IF NOT EXISTS claimed_amount  DOUBLE PRECISION DEFAULT 0,
    ADD COLUMN IF NOT EXISTS settled_amount  DOUBLE PRECISION DEFAULT 0,
    ADD COLUMN IF NOT EXISTS claim_status VARCHAR(255) DEFAULT 'SETTLED';

-- delete & add column in InsuranceConfiguration
ALTER TABLE IF EXISTS insurance_configuration
    DROP COLUMN IF EXISTS  preferred_hospital_url,
    ADD COLUMN IF NOT EXISTS  insurance_claim_link VARCHAR(255);

-- delete InsuranceRelation
DROP TABLE IF EXISTS insurance_relation;
