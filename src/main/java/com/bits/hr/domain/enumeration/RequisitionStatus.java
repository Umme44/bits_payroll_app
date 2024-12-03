package com.bits.hr.domain.enumeration;

/**
 * The RequisitionStatus enumeration.
 */
public enum RequisitionStatus {
    PENDING,
    LM_APPROVED,
    HOD_APPROVED,
    CTO_APPROVED,
    HOHR_VETTED,
    CEO_APPROVED,
    OPEN,
    CLOSED,
    PARTIALLY_CLOSED,
    NOT_APPROVED,

    IN_PROGRESS // only for PRF
}
