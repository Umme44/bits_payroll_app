package com.bits.hr.service.approvalProcess;

import java.util.List;

public interface ApprovalProcessService {
    boolean approveSelected(List<Long> selectedIds);

    boolean approveAll();

    boolean denySelected(List<Long> selectedIds);

    boolean denyAll();
}
