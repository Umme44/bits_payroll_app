package com.bits.hr.domain.enumeration;

/**
 * The RecruitmentNature enumeration.
 */
public enum RecruitmentNature {
    PLANNED_ADDITION, NEW_RECRUITMENT, REPLACEMENT;

    public static String recruitmentNatureEnumToNaturalText(RecruitmentNature recruitmentNature) {
        if (recruitmentNature == null) {
            return "";
        } else if (recruitmentNature.equals(RecruitmentNature.PLANNED_ADDITION)) {
            return "Planned Addition";
        } else if (recruitmentNature.equals(RecruitmentNature.NEW_RECRUITMENT)) {
            return "New Recruitment";
        } else if (recruitmentNature.equals(RecruitmentNature.REPLACEMENT)) {
            return "Replacement";
        } else {
            return "";
        }
    }
}
