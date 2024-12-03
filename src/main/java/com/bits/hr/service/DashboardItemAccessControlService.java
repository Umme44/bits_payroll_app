package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.DashboardItemAccessControlDTO;
import org.springframework.stereotype.Service;

public interface DashboardItemAccessControlService {
    public DashboardItemAccessControlDTO getAllAccessControl(long employeeId);

    public boolean canAccessAddressBook(Employee employee);

    public boolean canAccessAttendanceApprovals(Employee employee);

    public boolean canAccessLeaveApprovals(Employee employee);

    public boolean canAccessWorkFromHomeApprovals(Employee employee);

    public boolean canAccessFlexScheduleApprovals(Employee employee);

    public boolean canAccessMovementEntryApprovals(Employee employee);

    public boolean canAccessRRFApprovals(Employee employee);

    public boolean canAccessAttendanceTimeSheet(Employee employee);

    public boolean canAccessMovementEntry(Employee employee);

    public boolean canAccessMyTeam(Employee employee);

    public boolean canAccessBloodGroup(Employee employee);

    public boolean canAccessHoliday(Employee employee);

    public boolean canAccessLeave(Employee employee);

    public boolean canAccessMyStuff(Employee employee);

    public boolean canAccessPayslip(Employee employee);

    public boolean canAccessNominee(Employee employee);

    public boolean canAccessSalaryPaySlip(Employee employee);

    public boolean canAccessFestivalBonusPaySlip(Employee employee);

    public boolean canAccessGeneralNominee(Employee employee);

    public boolean canAccessGFNominee(Employee employee);

    public boolean canAccessPFNominee(Employee employee);

    public boolean canAccessIDCard(Employee employee);

    public boolean canAccessMyHierarchy(Employee employee);

    public boolean canAccessStatement(Employee employee);

    public boolean canAccessPFStatement(Employee employee);

    public boolean canAccessTaxStatement(Employee employee);

    public boolean canAccessTaxAcknowledgementReceiptStatement(Employee employee);

    public boolean canAccessInsurance(Employee employee);

    public boolean canAccessRequisition(Employee employee);

    public boolean canAccessRRFRequisition(Employee employee);

    public boolean canAccessCarRequisition(Employee employee);

    public boolean canAccessRoomRequisition(Employee employee);
}
