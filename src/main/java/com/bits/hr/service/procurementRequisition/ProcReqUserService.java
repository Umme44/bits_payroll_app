package com.bits.hr.service.procurementRequisition;

import com.bits.hr.service.dto.ProcReqMasterDTO;
import java.util.List;

public interface ProcReqUserService {
    List<ProcReqMasterDTO> employeeProcReqList(long employeeId);

    ProcReqMasterDTO createNewRequisition(ProcReqMasterDTO procReqMasterDTO);

    ProcReqMasterDTO updateRequisition(ProcReqMasterDTO procReqMasterDTO);
}
