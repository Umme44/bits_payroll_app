package com.bits.hr.service.importXL.employee.helperMethods.enumHelper;

import com.bits.hr.domain.enumeration.CardType;

public class CardTypeEnumFromString {

    CardType get(String s) {
        s = s.trim();
        switch (s) {
            case "CREDIT_CARD":
            case "Credit Card":
            case "Credit":
            case "credit":
                return CardType.CREDIT_CARD;
            case "DEBIT_CARD":
            case "Debit Card":
            case "Debit":
            case "debit":
                return CardType.DEBIT_CARD;
            case "PREPAID_CARD":
            case "Prepaid Card":
            case "Prepaid":
            case "prepaid":
                return CardType.PREPAID_CARD;
            default:
                return null;
        }
    }
}
