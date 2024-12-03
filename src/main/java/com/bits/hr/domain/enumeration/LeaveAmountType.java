package com.bits.hr.domain.enumeration;

/**
 * The LeaveAmountType enumeration.
 */
public enum LeaveAmountType {
    Day,
    Year;

    public static LeaveAmountType fromString(String x) {
        x = x.trim();
        switch (x) {
            case "Day":
                return Day;
            case "Year":
                return Year;
            default:
                return Year;
        }
    }
}
