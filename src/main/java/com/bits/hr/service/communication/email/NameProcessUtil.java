package com.bits.hr.service.communication.email;

import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.MaritalStatus;

public class NameProcessUtil {

    public static String getNameWithTitle(String name, Gender gender, MaritalStatus maritalStatus) {
        if (gender == Gender.MALE) {
            return String.format("Mr. %s", name);
        } else if (gender == Gender.FEMALE) {
            if (maritalStatus == MaritalStatus.MARRIED) {
                return String.format("Mrs. %s", name);
            } else {
                return String.format("Ms. %s", name);
            }
        } else {
            return name;
        }
    }
}
