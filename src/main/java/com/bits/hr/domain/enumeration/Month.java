package com.bits.hr.domain.enumeration;

/**
 * The Month enumeration.
 */
public enum Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    public static Month fromInteger(int x) {
        return Month.values()[x - 1];
    }

    public static int fromEnum(Month month) {
        return month.ordinal() + 1;
    }
}
