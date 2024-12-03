package com.bits.hr.domain.enumeration;

/**
 * The RequisitionResourceType enumeration.
 */
public enum RequisitionResourceType {
    BUDGET, NON_BUDGET;

    public static String requisitionResourceTypeToNaturalText(RequisitionResourceType requisitionResourceType) {
        if (requisitionResourceType == null) {
            return "";
        } else if (requisitionResourceType.equals(RequisitionResourceType.BUDGET)) {
            return "Budget";
        } else if (requisitionResourceType.equals(RequisitionResourceType.NON_BUDGET)) {
            return "Non-budget";
        } else {
            return "";
        }
    }
}
