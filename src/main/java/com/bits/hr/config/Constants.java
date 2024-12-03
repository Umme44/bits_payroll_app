package com.bits.hr.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final int DAYS_IN_WEEK = 7;

    public static final String NATURAL_TEXT_REGEX = "^[a-zA-Z0-9\\-@+_()#. ]*$";
    public static final String NUMERIC_REGEX = "^[0-9]*$";
    public static final String ALPHA_NUMERIC_REGEX = "^[a-zA-Z0-9 ]*$";
    public static final String ALPHABETS_REGEX = "^[a-zA-Z0-9 ]*$";
    public static final String PHONE_NUMBER_REGEX = "^(?:\\+88|88)?(01[3-9]\\d{8})$";

    private Constants() {}
}
