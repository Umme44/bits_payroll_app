package com.bits.hr.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class DashboardItemAccessControlDTO {

    private boolean canAccessAddressBook = true;

    private boolean canAccessAttendanceApprovals = true;

    private boolean canAccessLeaveApprovals = true;

    private boolean canAccessWorkFromHomeApprovals = true;

    private boolean canAccessFlexScheduleApprovals = true;

    private boolean canAccessMovementEntryApprovals = true;

    private boolean canAccessRRFApprovals = true;

    private boolean canAccessAttendanceTimeSheet = true;

    private boolean canAccessMovementEntry = true;

    private boolean canAccessMyTeam = true;

    private boolean canAccessBloodGroup = true;

    private boolean canAccessHoliday = true;

    private boolean canAccessLeave = true;

    private boolean canAccessMyStuff = true;

    private boolean canAccessPayslip = true;

    private boolean canAccessNominee = true;

    private boolean canAccessSalaryPaySlip = true;

    private boolean canAccessFestivalBonusPaySlip = true;

    private boolean canAccessGeneralNominee = true;

    private boolean canAccessGFNominee = true;

    private boolean canAccessPFNominee = true;

    private boolean canAccessIDCard = true;

    private boolean canAccessMyHierarchy = true;

    private boolean canAccessStatement = true;

    private boolean canAccessPFStatement = true;

    private boolean canAccessTaxStatement = true;

    private boolean canAccessTaxAcknowledgementReceiptStatement = true;

    private boolean canAccessInsurance = true;

    private boolean canAccessRequisition = true;

    private boolean canAccessRRFRequisition = true;

    private boolean canAccessCarRequisition = true;

    private boolean canAccessRoomRequisition = true;
}
