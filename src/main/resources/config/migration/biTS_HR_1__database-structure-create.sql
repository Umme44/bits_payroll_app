create sequence sequence_generator
    increment by 50 minvalue 1050;

create table if not exists jhi_user
(
    id bigint not null
    constraint jhi_user_pkey
    primary key,
    login varchar(50) not null
    constraint ux_user_login
    unique,
    password_hash varchar(60) not null,
    first_name varchar(50),
    last_name varchar(50),
    email varchar(191)
    constraint ux_user_email
    unique,
    image_url varchar(256),
    activated boolean not null,
    lang_key varchar(10),
    activation_key varchar(20),
    reset_key varchar(20),
    created_by varchar(50) not null,
    created_date timestamp,
    reset_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp
    );

create table if not exists jhi_authority
(
    name varchar(50) not null
    constraint jhi_authority_pkey
    primary key
    );

create table if not exists jhi_user_authority
(
    user_id bigint not null
    constraint fk_user_id
    references jhi_user,
    authority_name varchar(50) not null
    constraint fk_authority_name
    references jhi_authority,
    constraint jhi_user_authority_pkey
    primary key (user_id, authority_name)
    );

create table if not exists jhi_persistent_audit_event
(
    event_id bigint not null
    constraint jhi_persistent_audit_event_pkey
    primary key,
    principal varchar(50) not null,
    event_date timestamp,
    event_type varchar(255)
    );

create index if not exists idx_persistent_audit_event
	on jhi_persistent_audit_event (principal, event_date);

create table if not exists jhi_persistent_audit_evt_data
(
    event_id bigint not null
    constraint fk_evt_pers_audit_evt_data
    references jhi_persistent_audit_event,
    name varchar(150) not null,
    value varchar(255),
    constraint jhi_persistent_audit_evt_data_pkey
    primary key (event_id, name)
    );

create index if not exists idx_persistent_audit_evt_data
	on jhi_persistent_audit_evt_data (event_id);

create table if not exists designation
(
    id bigint not null
    constraint designation_pkey
    primary key,
    designation_name varchar(250) not null
    constraint ux_designation_designation_name
    unique
    );

create table if not exists department
(
    id bigint not null
    constraint department_pkey
    primary key,
    department_name varchar(250) not null
    constraint ux_department_department_name
    unique
    );

create table if not exists nationality
(
    id bigint not null
    constraint nationality_pkey
    primary key,
    nationality_name varchar(25) not null
    constraint ux_nationality_nationality_name
    unique
    );

create table if not exists band
(
    id bigint not null
    constraint band_pkey
    primary key,
    band_name varchar(250) not null
    constraint ux_band_band_name
    unique,
    min_salary double precision not null,
    max_salary double precision not null,
    welfare_fund double precision,
    mobile_celling double precision,
    created_at date,
    updated_at date,
    created_by_id bigint
    constraint fk_band_created_by_id
    references jhi_user,
    updated_by_id bigint
    constraint fk_band_updated_by_id
    references jhi_user
    );

create table if not exists bank_branch
(
    id bigint not null
    constraint bank_branch_pkey
    primary key,
    branch_name varchar(25) not null
    constraint ux_bank_branch_branch_name
    unique
    );

create table if not exists jhi_references
(
    id bigint not null
    constraint jhi_references_pkey
    primary key,
    name varchar(255),
    institute varchar(255),
    designation varchar(255),
    relationship_with_employee varchar(255),
    email varchar(255),
    contact_number varchar(255)
    );

create table if not exists salary_generator_master
(
    id bigint not null
    constraint salary_generator_master_pkey
    primary key,
    year varchar(255),
    month varchar(255),
    is_generated boolean,
    is_mobile_bill_imported boolean,
    is_pf_loan_repayment_imported boolean,
    is_attendance_imported boolean
    );

create table if not exists year_month
(
    id bigint not null
    constraint year_month_pkey
    primary key,
    month integer,
    year integer
);

create table if not exists holidays
(
    id bigint not null
    constraint holidays_pkey
    primary key,
    holiday_type varchar(255),
    description varchar(255),
    start_date date,
    end_date date
    );

create table if not exists unit
(
    id bigint not null
    constraint unit_pkey
    primary key,
    unit_name varchar(255) not null
    constraint ux_unit_unit_name
    unique
    );

create table if not exists employee
(
    id bigint not null
    constraint employee_pkey
    primary key,
    reference_id varchar(255),
    pin varchar(255) not null
    constraint ux_employee_pin
    unique,
    picture varchar(255),
    full_name varchar(255),
    sur_name varchar(255),
    national_id_no varchar(255),
    date_of_birth date,
    place_of_birth varchar(255),
    father_name varchar(255),
    mother_name varchar(255),
    blood_group varchar(255),
    present_address varchar(255),
    permanent_address varchar(255),
    personal_contact_no varchar(255),
    personal_email varchar(255),
    religion varchar(255),
    marital_status varchar(255),
    date_of_marriage date,
    spouse_name varchar(255),
    official_email varchar(255),
    official_contact_no varchar(255),
    office_phone_extension varchar(255),
    whatsapp_id varchar(255),
    skype_id varchar(255),
    emergency_contact_person_name varchar(255),
    emergency_contact_person_relationship_with_employee varchar(255),
    emergency_contact_person_contact_number varchar(255),
    main_gross_salary double precision,
    employee_category varchar(255),
    location varchar(255),
    office_time_duration double precision,
    check_in_time timestamp,
    check_out_time timestamp,
    date_of_joining date,
    date_of_confirmation date,
    is_probationary_period_extended boolean,
    probation_period_extended_to date,
    pay_type varchar(255),
    disbursement_method varchar(255),
    bank_name varchar(255),
    bank_account_no varchar(255),
    mobile_celling bigint,
    bkash_number varchar(255),
    card_type varchar(255),
    card_number varchar(255),
    tin_number varchar(255),
    passport_no varchar(255),
    passport_place_of_issue varchar(255),
    passport_issued_date date,
    passport_expiry_date date,
    gender varchar(255),
    welfare_fund_deduction double precision,
    employment_status varchar(255),
    has_disabled_child boolean,
    is_first_time_ait_giver boolean,
    is_salary_hold boolean,
    is_festival_bonus_hold boolean,
    is_physically_disabled boolean,
    is_freedom_fighter boolean,
    has_over_time boolean,
    probation_period_end_date date,
    contructual_period_end_date date,
    contructual_period integer,
    is_contructual_period_extended boolean,
    contract_period_extended_to date,
    living_allowance double precision,
    contract_period_end_date date,
    phrobation_period_end_date date,
    designation_id bigint
    constraint fk_employee_designation_id
    references designation,
    department_id bigint
    constraint fk_employee_department_id
    references department,
    reporting_to_id bigint
    constraint fk_employee_reporting_to_id
    references employee,
    nationality_id bigint
    constraint fk_employee_nationality_id
    references nationality,
    bank_branch_id bigint
    constraint fk_employee_bank_branch_id
    references bank_branch,
    band_id bigint
    constraint fk_employee_band_id
    references band,
    unit_id bigint
    constraint fk_employee_unit_id
    references unit,
    user_id bigint
    constraint ux_employee_user_id
    unique
    constraint fk_employee_user_id
    references jhi_user
    );

create table if not exists education_details
(
    id bigint not null
    constraint education_details_pkey
    primary key,
    name_of_degree varchar(255),
    subject varchar(255),
    institute varchar(255),
    year_of_degree_completion varchar(255),
    employee_id bigint
    constraint fk_education_details_employee_id
    references employee
    );

create table if not exists working_experience
(
    id bigint not null
    constraint working_experience_pkey
    primary key,
    last_designation varchar(255),
    organization_name varchar(255),
    doj_of_last_organization date,
    dor_of_last_organization date,
    employee_id bigint
    constraint fk_working_experience_employee_id
    references employee
    );

create table if not exists training_history
(
    id bigint not null
    constraint training_history_pkey
    primary key,
    training_name varchar(255),
    coordinated_by varchar(255),
    date_of_completion date,
    employee_id bigint
    constraint fk_training_history_employee_id
    references employee
    );

create table if not exists employment_history
(
    id bigint not null
    constraint employment_history_pkey
    primary key,
    reference_id varchar(255),
    pin varchar(255),
    event_type varchar(255),
    effective_date date,
    previous_main_gross_salary double precision,
    current_main_gross_salary double precision,
    previous_working_hour varchar(255),
    changed_working_hour varchar(255),
    is_modifiable boolean,
    previous_designation_id bigint
    constraint fk_employment_history_previous_designation_id
    references designation,
    changed_designation_id bigint
    constraint fk_employment_history_changed_designation_id
    references designation,
    previous_department_id bigint
    constraint fk_employment_history_previous_department_id
    references department,
    changed_department_id bigint
    constraint fk_employment_history_changed_department_id
    references department,
    previous_reporting_to_id bigint
    constraint fk_employment_history_previous_reporting_to_id
    references employee,
    changed_reporting_to_id bigint
    constraint fk_employment_history_changed_reporting_to_id
    references employee,
    employee_id bigint
    constraint fk_employment_history_employee_id
    references employee,
    previous_unit_id bigint
    constraint fk_employment_history_previous_unit_id
    references unit,
    changed_unit_id bigint
    constraint fk_employment_history_changed_unit_id
    references unit,
    previous_band_id bigint
    constraint fk_employment_history_previous_band_id
    references band,
    changed_band_id bigint
    constraint fk_employment_history_changed_band_id
    references band
    );

create table if not exists attendance_summary
(
    id bigint not null
    constraint attendance_summary_pkey
    primary key,
    month integer,
    year integer,
    total_working_days integer,
    total_leave_days integer,
    total_absent_days integer,
    total_fraction_days integer,
    employee_id bigint
    constraint fk_attendance_summary_employee_id
    references employee
);

create table if not exists mobile_bill
(
    id bigint not null
    constraint mobile_bill_pkey
    primary key,
    month integer,
    amount double precision,
    year integer,
    employee_id bigint
    constraint fk_mobile_bill_employee_id
    references employee
);

create table if not exists employee_salary
(
    id bigint not null
    constraint employee_salary_pkey
    primary key,
    month varchar(255),
    year integer,
    salary_generation_date date,
    created_by varchar(255),
    created_at timestamp,
    updated_by varchar(255),
    updated_at timestamp,
    ref_pin varchar(255),
    pin varchar(255),
    joining_date date,
    confirmation_date date,
    employee_category varchar(255),
    unit varchar(255),
    department varchar(255),
    main_gross_salary double precision,
    main_gross_basic_salary double precision,
    main_gross_house_rent double precision,
    main_gross_medical_allowance double precision,
    main_gross_conveyance_allowance double precision,
    absent_days integer,
    fraction_days integer,
    payable_gross_salary double precision,
    payable_gross_basic_salary double precision,
    payable_gross_house_rent double precision,
    payable_gross_medical_allowance double precision,
    payable_gross_conveyance_allowance double precision,
    arrear_salary double precision,
    pf_deduction double precision,
    tax_deduction double precision,
    welfare_fund_deduction double precision,
    mobile_bill_deduction double precision,
    other_deduction double precision,
    total_deduction double precision,
    net_pay double precision,
    remarks varchar(255),
    pf_contribution double precision,
    gf_contribution double precision,
    provision_for_festival_bonus double precision,
    provision_for_leave_encashment double precision,
    provishion_for_project_bonus double precision,
    is_finalized boolean,
    is_dispatched boolean,
    entertainment double precision,
    utility double precision,
    living_allowance double precision,
    other_addition double precision,
    salary_adjustment double precision,
    provident_fund_arrear double precision,
    employee_id bigint
    constraint fk_employee_salary_employee_id
    references employee
    );

create table if not exists attendance_entry
(
    id bigint not null
    constraint attendance_entry_pkey
    primary key,
    date date,
    in_time timestamp,
    in_note varchar(255),
    out_time timestamp,
    out_note varchar(255),
    status varchar(255),
    employee_id bigint
    constraint fk_attendance_entry_employee_id
    references employee
    );

create table if not exists leave_application
(
    id bigint not null
    constraint leave_application_pkey
    primary key,
    application_date date,
    leave_type varchar(255),
    description varchar(255),
    start_date date,
    end_date date,
    is_line_manager_approved boolean,
    is_hr_approved boolean,
    is_rejected boolean,
    rejection_comment varchar(255),
    is_half_day boolean,
    duration_in_day integer,
    phone_number_on_leave varchar(255),
    address_on_leave text,
    reason text,
    employee_id bigint
    constraint fk_leave_application_employee_id
    references employee
    );

create table if not exists leave_balance
(
    id bigint not null
    constraint leave_balance_pkey
    primary key,
    leave_type varchar(255),
    opening_balance integer,
    closing_balance integer,
    consumed_during_year integer,
    year integer,
    amount integer,
    leave_amount_type varchar(255),
    employee_id bigint
    constraint fk_leave_balance_employee_id
    references employee
    );

create table if not exists attendance
(
    id bigint not null
    constraint attendance_pkey
    primary key,
    year integer,
    month integer,
    absent_days integer,
    fraction_days integer,
    compensatory_leave_gained integer,
    employee_id bigint
    constraint fk_attendance_employee_id
    references employee
);

create table if not exists generated_salary_history
(
    id bigint not null
    constraint generated_salary_history_pkey
    primary key,
    year integer,
    month integer,
    status boolean
);

create table if not exists leave_allocation
(
    id bigint not null
    constraint leave_allocation_pkey
    primary key,
    year integer not null,
    leave_type varchar(255),
    allocated_days integer not null
    );

create table if not exists pf_account
(
    id bigint not null
    constraint pf_account_pkey
    primary key
    constraint fk_pf_account_employee_id
    references employee,
    pf_code varchar(255),
    membership_start_date date,
    membership_end_date date,
    status varchar(255)
    );

create table if not exists pf_collection
(
    id bigint not null
    constraint pf_collection_pkey
    primary key,
    employee_contribution double precision,
    employer_contribution double precision,
    transaction_date date,
    year integer,
    month integer,
    collection_type varchar(255),
    pf_account_id bigint
    constraint fk_pf_collection_pf_account_id
    references pf_account
    );

create table if not exists pf_nominee
(
    id bigint not null
    constraint pf_nominee_pkey
    primary key,
    nomination_date date,
    full_name varchar(255),
    present_address varchar(255),
    permanent_address varchar(255),
    relationship varchar(255),
    date_of_birth date,
    age integer,
    share_percentage double precision,
    nid_number varchar(255),
    passport_number varchar(255),
    brn_number varchar(255),
    photo varchar(255),
    guardian_name varchar(255),
    guardian_father_or_spouse_name varchar(255),
    guardian_date_of_birth date,
    guardian_present_address varchar(255),
    guardian_permanent_address varchar(255),
    guardian_proof_of_identity_of_legal_guardian varchar(255),
    guardian_relationship_with_nominee varchar(255),
    guardian_nid_number varchar(255),
    gurdian_brn_number varchar(255),
    guardian_id_number varchar(255),
    pf_account_id bigint
    constraint fk_pf_nominee_pf_account_id
    references pf_account,
    pf_witness_id bigint
    constraint fk_pf_nominee_pf_witness_id
    references employee,
    approved_by_id bigint
    constraint fk_pf_nominee_approved_by_id
    references employee
    );

create table if not exists pf_loan_application
(
    id bigint not null
    constraint pf_loan_application_pkey
    primary key,
    dibrushment_amount double precision,
    dibrushment_date date,
    installment_amount double precision,
    no_of_installment integer,
    remarks varchar(255),
    is_recommended boolean,
    recommendation_date date,
    is_approved boolean,
    approval_date date,
    is_rejected boolean,
    rejection_reason varchar(255),
    rejection_date date,
    recommended_by_id bigint
    constraint fk_pf_loan_application_recommended_by_id
    references employee,
    approved_by_id bigint
    constraint fk_pf_loan_application_approved_by_id
    references employee,
    rejected_by_id bigint
    constraint fk_pf_loan_application_rejected_by_id
    references employee,
    pf_account_id bigint
    constraint fk_pf_loan_application_pf_account_id
    references pf_account
    );

create table if not exists pf_loan
(
    id bigint not null
    constraint pf_loan_pkey
    primary key,
    disbursement_amount double precision,
    disbursement_date date,
    bank_name varchar(255),
    bank_branch varchar(255),
    bank_account_number varchar(255),
    cheque_number varchar(255),
    instalment_number varchar(255),
    installment_amount double precision,
    instalment_start_from date,
    status varchar(255),
    employee_id bigint
    constraint fk_pf_loan_employee_id
    references employee,
    pf_loan_application_id bigint
    constraint fk_pf_loan_pf_loan_application_id
    references pf_loan_application,
    pf_account_id bigint
    constraint fk_pf_loan_pf_account_id
    references pf_account
    );

create table if not exists pf_loan_repayment
(
    id bigint not null
    constraint pf_loan_repayment_pkey
    primary key,
    amount double precision,
    status varchar(255),
    deduction_month integer,
    deduction_year integer,
    deduction_date date,
    pf_loan_id bigint
    constraint fk_pf_loan_repayment_pf_loan_id
    references pf_loan,
    employee_id bigint
    constraint fk_pf_loan_repayment_employee_id
    references employee
    );

create table if not exists ait_config
(
    id bigint not null
    constraint ait_config_pkey
    primary key,
    start_date date not null,
    end_date date not null,
    tax_config text
);

create table if not exists employee_resignation
(
    id bigint not null
    constraint employee_resignation_pkey
    primary key,
    created_at date,
    updated_at date,
    resignation_date date,
    approval_status varchar(255),
    approval_comment varchar(255),
    is_salary_hold boolean,
    is_festival_bonus_hold boolean,
    resignation_reason varchar(255),
    created_by_id bigint
    constraint fk_employee_resignation_created_by_id
    references employee,
    uodated_by_id bigint
    constraint fk_employee_resignation_uodated_by_id
    references employee,
    employee_id bigint
    constraint fk_employee_resignation_employee_id
    references employee
    );

create table if not exists festival
(
    id bigint not null
    constraint festival_pkey
    primary key,
    festival_name varchar(255) not null
    );

create table if not exists calender_year
(
    id bigint not null
    constraint calender_year_pkey
    primary key,
    year integer not null
    constraint ux_calender_year_year
    unique,
    start_date date not null,
    end_date date not null
);

create table if not exists festival_bonus_config
(
    id bigint not null
    constraint festival_bonus_config_pkey
    primary key,
    employee_category varchar(255) not null
    constraint ux_festival_bonus_config_employee_category
    unique,
    calculate_from varchar(255) not null,
    disbursement_type varchar(255) not null,
    percentage integer
    );

create table if not exists festival_bonus
(
    id bigint not null
    constraint festival_bonus_pkey
    primary key,
    created_by varchar(255),
    created_at date,
    updated_by varchar(255),
    updated_at date,
    festival_date date,
    disburse_amount_percentage integer,
    is_disbursed_with_salary boolean,
    disburse_salary_year integer,
    disburse_salary_month integer,
    disbursement_date date,
    employee_category varchar(255),
    employee_religion varchar(255),
    is_fixed_amount boolean,
    fixed_amount double precision,
    is_on_pro_data_basis boolean,
    is_disbursed boolean,
    disbruised_by varchar(255),
    is_finalized boolean,
    finalized_by varchar(255),
    festival_id bigint
    constraint fk_festival_bonus_festival_id
    references festival,
    calender_year_id bigint
    constraint fk_festival_bonus_calender_year_id
    references calender_year
    );

create table if not exists festival_bonus_details
(
    id bigint not null
    constraint festival_bonus_details_pkey
    primary key,
    bonus_amount double precision not null,
    employee_id bigint not null
    constraint fk_festival_bonus_details_employee_id
    references employee,
    festival_bonus_id bigint
    constraint fk_festival_bonus_details_festival_bonus_id
    references festival_bonus
);

create table if not exists attendance_sync_cache
(
    id bigint not null
    constraint attendance_sync_cache_pkey
    primary key,
    employee_pin varchar(255),
    timestamp timestamp,
    terminal integer
    );

create table if not exists "Z_FLYWAY_MIGRATION_CHANGELOG"
(
    installed_rank integer not null
    constraint "Z_FLYWAY_MIGRATION_CHANGELOG_pk"
    primary key,
    version varchar(50),
    description varchar(200) not null,
    type varchar(20) not null,
    script varchar(1000) not null,
    checksum integer,
    installed_by varchar(100) not null,
    installed_on timestamp default now() not null,
    execution_time integer not null,
    success boolean not null
    );

create index if not exists "Z_FLYWAY_MIGRATION_CHANGELOG_s_idx"
	on "Z_FLYWAY_MIGRATION_CHANGELOG" (success);

