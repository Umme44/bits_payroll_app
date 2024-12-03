-- organization table creation script
create table ORGANIZATION
(
    ID                                  BIGINT       not null
        primary key,
    CONTACT_NUMBER                      VARCHAR(255),
    DOCUMENT_LETTER_HEAD                TEXT,
    DOMAIN_NAME                         VARCHAR(255) not null,
    EMAIL_ADDRESS                       VARCHAR(255) not null,
    FACEBOOK                            VARCHAR(255),
    FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD  TEXT,
    FINANCE_MANAGER_PIN                 VARCHAR(255),
    FINANCE_MANAGER_SIGNATURE           TEXT,
    HAS_ORGANIZATION_STAMP              BOOLEAN,
    HR_EMAIL_ADDRESS                    VARCHAR(255),
    INSTAGRAM                           VARCHAR(255),
    LINKEDIN                            VARCHAR(255),
    SHORT_NAME                          VARCHAR(255) not null,
    FULL_NAME                           VARCHAR(255) not null,
    NO_REPLY_EMAIL_ADDRESS              VARCHAR(255),
    NOMINEE_LETTER_HEAD                 TEXT,
    ORGANIZATION_STAMP                  TEXT,
    PF_STATEMENT_LETTER_HEAD            TEXT,
    RECRUITMENT_REQUISITION_LETTER_HEAD TEXT,
    SALARY_PAYSLIP_LETTER_HEAD          TEXT,
    SLOGAN                              VARCHAR(255),
    LOGO                                TEXT,
    TAX_STATEMENT_LETTER_HEAD           TEXT,
    TWITTER                             VARCHAR(255),
    WHATSAPP                            VARCHAR(255),
    YOUTUBE                             VARCHAR(255)
);

