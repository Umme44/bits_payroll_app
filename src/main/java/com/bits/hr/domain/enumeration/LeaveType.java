package com.bits.hr.domain.enumeration;

/**
 * The LeaveType enumeration.
 */
public enum LeaveType {
    //Mentionable_Annual_Leave, Mentionable_Casual_Leave, Non_Mentionable_Compensatory_Leave, Non_Mentionable_Pandemic_Leave, Non_Mentionable_Paternity_Leave, Non_Mentionable_Maternity_Leave, Other;
    MENTIONABLE_ANNUAL_LEAVE("Annual Leave"),
    MENTIONABLE_CASUAL_LEAVE("Casual Leave"),
    NON_MENTIONABLE_COMPENSATORY_LEAVE("Compensatory Leave"),
    NON_MENTIONABLE_PANDEMIC_LEAVE("Pandemic Leave"),
    NON_MENTIONABLE_PATERNITY_LEAVE("Paternity Leave"),
    NON_MENTIONABLE_MATERNITY_LEAVE("Maternity Leave"),
    LEAVE_WITHOUT_PAY("Leave without Pay"),
    LEAVE_WITHOUT_PAY_SANDWICH("Leave without Pay Sandwich"),
    OTHER("Other");

    private final String value;

    LeaveType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
