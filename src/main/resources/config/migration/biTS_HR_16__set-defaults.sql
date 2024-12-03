alter table employee
    alter
        column is_probationary_period_extended
        set default false,
    alter
        column has_disabled_child
        set default false,
    alter
        column is_first_time_ait_giver
        set default false,
    alter
        column is_salary_hold
        set default false,
    alter
        column is_festival_bonus_hold
        set default false,
    alter
        column is_physically_disabled
        set default false,
    alter
        column is_freedom_fighter
        set default false,
    alter
        column has_over_time
        set default false;

alter table employee_salary
    alter
        column is_finalized
        set default false,
    alter
        column is_dispatched
        set default false;
