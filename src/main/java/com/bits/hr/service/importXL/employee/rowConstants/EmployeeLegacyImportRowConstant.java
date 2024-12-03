package com.bits.hr.service.importXL.employee.rowConstants;

public class EmployeeLegacyImportRowConstant {

    //    A   0   sl XX
    //    B   1   pin
    //    C   2   fullName
    //    D   3   band
    //    E   4   designation
    //
    //    F   5   unit
    //    G   6   dept
    //    H   7   LM pin

    public static final int LRC_PIN = 1;
    public static final int LRC_FULL_NAME = 2;
    public static final int LRC_BAND = 3;
    public static final int LRC_DESIGNATION = 4;
    public static final int LRC_UNIT = 5;
    public static final int LRC_DEPT = 6;
    public static final int LRC_LM_PIN = 7;

    //8   9   10  11  12  13  14  15  16
    //I   J   K   L   M   N   O   P   Q   == SKIP

    public static final int LRC_LOCATION = 17;
    public static final int LRC_FLOOR = 18;
    public static final int LRC_SPACE = 19;

    public static final int LRC_OFFICIAL_EMAIL = 20;
    public static final int LRC_PERSONAL_EMAIL = 21;
    public static final int LRC_CELL = 22;
    public static final int LRC_WHATSSUP = 23;
    public static final int LRC_EMERGENCY_CONTRACT = 24;
    public static final int LRC_SKYPE = 25;
    public static final int LRC_JOB_STARTING_DATE = 26;
    public static final int LRC_Doj = 27;
    public static final int LRC_DoC_OR_CONTRACT_END = 28;
    public static final int LRC_DoB = 29;
    public static final int LRC_BLOOD_GROUP = 30;

    //    R   17   Location
    //    S   18   floor
    //    T   19   space
    //
    //    U   20  official email
    //    V   21  personal email
    //    W   22  cell
    //    X   23  whatssup
    //    Y   24  emergency contract
    //    Z   25  skype
    //    AA  26  job started ( in work life )  XX
    //    AB  27  bits join ( doj )
    //    AC  28  DoC/Contract End
    //    AD  29  DoB
    //    AE  30  Blood Group

    public static final int LRC_GENDER = 31;
    public static final int LRC_MARITAL_STATUS = 32;
    public static final int LRC_RELIGION = 33;
    public static final int LRC_NID = 34;

    public static final int LRC_EMPLOYEE_CATEGORY = 36;
    //
    //                AF   31  Gender
    //                AG   32  Status ( Martial)
    //                AH   33  Religion
    //                AI   34  NID
    //
    //                AJ   35  Job Status XX
    //
    //                AK   36  Contract Status // EMPLOYEE CATEGORY
    //
    //                AL   37  biTS Duration  XX
    //                AM   38  biTS Years     XX
    //                AN   39  Total duration XX
    //                AO   40  Total Years    XX
    //                AP   41  Remarks XXX
    //                AP   42  Gross Salary

    public static final int LRC_GROSS_SALARY = 42;
    public static final int LRC_MOBILE_CELLING = 43;
    public static final int LRC_WELFARE_FUND = 44;
}
