package com.bits.hr.domain.enumeration;

public enum AttendanceStatus {
    BLANK,
    BLANK_INEFFECTIVE,
    GOVT_HOLIDAY,
    SPECIAL_HOLIDAY,
    WEEKLY_OFFDAY,
    PRESENT,
    PRESENT_GOVT_HOLIDAY,
    PRESENT_WEEKLY_OFFDAY,
    NON_FULFILLED_OFFICE_HOURS,
    LATE,
    ABSENT,
    MENTIONABLE_ANNUAL_LEAVE,
    MENTIONABLE_CASUAL_LEAVE,
    NON_MENTIONABLE_COMPENSATORY_LEAVE,
    NON_MENTIONABLE_PANDEMIC_LEAVE,
    NON_MENTIONABLE_PATERNITY_LEAVE,
    NON_MENTIONABLE_MATERNITY_LEAVE,
    LEAVE,
    LEAVE_WITHOUT_PAY,
    OTHER,
    MOVEMENT,
    GENERAL_HOLIDAY;

    public static AttendanceStatus fromString(String x) {
        x = x.trim().toUpperCase();

        switch (x) {
            case "BLANK":
                return BLANK;
            case "WEEKLY_OFFDAY":
                return WEEKLY_OFFDAY;
            case "PRESENT":
                return PRESENT;
            case "NON_FULFILLED_OFFICE_HOURS":
                return NON_FULFILLED_OFFICE_HOURS;
            case "LATE":
                return LATE;
            case "ABSENT":
                return ABSENT;
            case "MENTIONABLE_ANNUAL_LEAVE":
                return MENTIONABLE_ANNUAL_LEAVE;
            case "MENTIONABLE_CASUAL_LEAVE":
                return MENTIONABLE_CASUAL_LEAVE;
            case "NON_MENTIONABLE_COMPENSATORY_LEAVE":
                return NON_MENTIONABLE_COMPENSATORY_LEAVE;
            case "NON_MENTIONABLE_PANDEMIC_LEAVE":
                return NON_MENTIONABLE_PANDEMIC_LEAVE;
            case "NON_MENTIONABLE_PATERNITY_LEAVE":
                return NON_MENTIONABLE_PATERNITY_LEAVE;
            case "NON_MENTIONABLE_MATERNITY_LEAVE":
                return NON_MENTIONABLE_MATERNITY_LEAVE;
            case "MOVEMENT":
                return MOVEMENT;
            default:
                return OTHER;
        }
    }

    public static String toString(AttendanceStatus attendanceStatus) {
        switch (attendanceStatus) {
            case WEEKLY_OFFDAY:
                return "Weekly Off Day";
            case PRESENT:
                return "Present";
            case NON_FULFILLED_OFFICE_HOURS:
                return "Non Fulfilled Office Hours";
            case LATE:
                return "Late";
            case ABSENT:
                return "Absent";
            case MENTIONABLE_ANNUAL_LEAVE:
                return "Mentionable Annual Leave";
            case MENTIONABLE_CASUAL_LEAVE:
                return "Mentionable Casual Leave";
            case NON_MENTIONABLE_COMPENSATORY_LEAVE:
                return "Non Mentionable Compensatory Leave";
            case NON_MENTIONABLE_PANDEMIC_LEAVE:
                return "Non Mentionable Pandemic Leave";
            case NON_MENTIONABLE_PATERNITY_LEAVE:
                return "Non Mentionable Paternity Leave";
            case NON_MENTIONABLE_MATERNITY_LEAVE:
                return "Non Mentionable Maternity Leave";
            case MOVEMENT:
                return "Movement";
            default:
                return "Other";
        }
    }
}
