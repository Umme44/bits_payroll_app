package com.bits.hr.util;

import com.bits.hr.domain.enumeration.CardType;
import com.bits.hr.domain.enumeration.DisbursementMethod;
import com.bits.hr.domain.enumeration.LeaveType;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnumUtil {

    public static CardType getCardTypeFromString(String str) {
        switch (str) {
            case "debit card":
            case "Debit Card":
            case "Debit":
            case "debit":
            case "DEBIT_CARD":
                return CardType.DEBIT_CARD;
            case "credit card":
            case "Credit Card":
            case "Credit":
            case "credit":
            case "CREDIT_CARD":
                return CardType.CREDIT_CARD;
            case "prepaid card":
            case "Prepaid Card":
            case "Prepaid":
            case "prepaid":
            case "PREPAID_CARD":
                return CardType.PREPAID_CARD;
            default:
                return null;
        }
    }

    public static DisbursementMethod getDisbursementMethodFromString(String str) {
        switch (str) {
            case "bank":
            case "Bank":
            case "BANK":
                return DisbursementMethod.BANK;
            case "cash":
            case "Cash":
            case "CASH":
                return DisbursementMethod.CASH;
            case "Mobile Banking":
            case "Mobile banking":
            case "mobile banking":
            case "Mobile":
            case "mobile":
            case "MOBILE_BANKING":
                return DisbursementMethod.MOBILE_BANKING;
            default:
                return null;
        }
    }

    public static boolean getBooleanFromString(String string) throws Exception {
        string = string.trim().toUpperCase(Locale.ROOT);
        switch (string) {
            case "YES":
            case "TRUE":
                return true;
            case "NO":
            case "FALSE":
                return false;
            default:
                return string.toUpperCase(Locale.ROOT).startsWith("T");
        }
    }

    public static LeaveType getLeaveTypeFromString(String str) {
        str = str.trim().toUpperCase(Locale.ROOT);
        switch (str) {
            case "MENTIONABLE_ANNUAL_LEAVE":
            case "ANNUAL_LEAVE":
            case "ANNUAL LEAVE":
            case "ANNUAL":
                return LeaveType.MENTIONABLE_ANNUAL_LEAVE;
            case "MENTIONABLE_CASUAL_LEAVE":
            case "CASUAL_LEAVE":
            case "CASUAL LEAVE":
            case "CASUAL":
                return LeaveType.MENTIONABLE_CASUAL_LEAVE;
            case "NON_MENTIONABLE_COMPENSATORY_LEAVE":
            case "COMPENSATORY_LEAVE":
            case "COMPENSATORY LEAVE":
            case "COMPENSATORY":
                return LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE;
            case "PANDEMIC_LEAVE":
            case "PANDEMIC LEAVE":
            case "PANDEMIC":
                return LeaveType.NON_MENTIONABLE_PANDEMIC_LEAVE;
            case "NON_MENTIONABLE_PATERNITY_LEAVE":
            case "PATERNITY_LEAVE":
            case "PATERNITY LEAVE":
            case "PATERNITY":
                return LeaveType.NON_MENTIONABLE_PATERNITY_LEAVE;
            case "NON_MENTIONABLE_MATERNITY_LEAVE":
            case "MATERNITY_LEAVE":
            case "MATERNITY LEAVE":
            case "MATERNITY":
                return LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE;
            default:
                return null;
        }
    }
}
