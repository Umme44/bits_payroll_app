package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.DashboardItemAccessControlDTO;
import com.bits.hr.service.dto.NavBarItemAccessControlDTO;

public interface NavBarItemAccessControlService {
    public NavBarItemAccessControlDTO getAllAccessControl(long employeeId);

    public boolean canManageTacAcknowledgementReceipt(Employee employee);
}
