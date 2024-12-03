package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.DashboardItemAccessControlService;
import com.bits.hr.service.dto.DashboardItemAccessControlDTO;
import org.springframework.stereotype.Service;

@Service
public class DashboardItemAccessControlServiceImpl implements DashboardItemAccessControlService {

    private final EmployeeRepository employeeRepository;

    public DashboardItemAccessControlServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public DashboardItemAccessControlDTO getAllAccessControl(long employeeId) {
        DashboardItemAccessControlDTO dashboardItemAccessControlDTO = new DashboardItemAccessControlDTO();
        Employee employee = employeeRepository.findById(employeeId).get();
        return dashboardItemAccessControlDTO;
    }

    @Override
    public boolean canAccessAddressBook(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessAttendanceApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessLeaveApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessWorkFromHomeApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessFlexScheduleApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessMovementEntryApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessRRFApprovals(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessAttendanceTimeSheet(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessMovementEntry(Employee employee) {
        return false;
    }

    @Override
    public boolean canAccessMyTeam(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessBloodGroup(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessHoliday(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessLeave(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessMyStuff(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessPayslip(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessNominee(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessSalaryPaySlip(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessFestivalBonusPaySlip(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessGeneralNominee(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessGFNominee(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessPFNominee(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessIDCard(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessMyHierarchy(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessStatement(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessPFStatement(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessTaxStatement(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessTaxAcknowledgementReceiptStatement(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessInsurance(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessRequisition(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessRRFRequisition(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessCarRequisition(Employee employee) {
        return true;
    }

    @Override
    public boolean canAccessRoomRequisition(Employee employee) {
        return true;
    }
}
