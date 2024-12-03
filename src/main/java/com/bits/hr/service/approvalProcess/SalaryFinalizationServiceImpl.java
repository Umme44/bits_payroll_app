package com.bits.hr.service.approvalProcess;

import java.util.List;

public class SalaryFinalizationServiceImpl implements ApprovalProcessService {

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        return false;
    }

    @Override
    public boolean approveAll() {
        return false;
    }

    @Override
    public boolean denySelected(List<Long> selectedIds) {
        return false;
    }

    @Override
    public boolean denyAll() {
        return false;
    }
}
