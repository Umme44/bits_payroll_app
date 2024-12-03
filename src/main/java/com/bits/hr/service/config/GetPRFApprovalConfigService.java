package com.bits.hr.service.config;

import com.bits.hr.domain.Config;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import java.util.Map;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPRFApprovalConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private EmployeeService employeeService;

    public static final String PRF_APPROVAL_02_NAME = "Technical";
    public static final String PRF_APPROVAL_03_NAME = "Operation";
    public static final String PRF_APPROVAL_04_NAME = "Finance";
    public static final String PRF_APPROVAL_05_NAME = "CEO";

    public Map<String, Long> getApprovalFlowEmployeeList() {
        //Technical-100,Operation-100,Finance-200,CEO-220
        Config config = configRepository.findConfigByKey(DefinedKeys.PRF_APPROVAL_FLOW).get();

        String[] approvalFlowList = config.getValue().split(",");
        Map<String, Long> approvalFlowEmployeeList = new HashedMap<>();
        for (String approvalFlow : approvalFlowList) {
            String[] singleFlow = approvalFlow.split("-");
            approvalFlowEmployeeList.put(singleFlow[0], employeeService.findEmployeeByPin(singleFlow[1]).get().getId());
        }
        return approvalFlowEmployeeList;
    }

    public void fillPRFApprovals(ProcReqMasterDTO procReqMasterDTO) {
        Map<String, Long> approvalFlowEmployeeList = getApprovalFlowEmployeeList();

        // 1st dept. head
        Long deptHeadId = employeeService.findDeptHeadByDeptId(procReqMasterDTO.getDepartmentId()).getId();
        procReqMasterDTO.setRecommendedBy01Id(deptHeadId);
        // 2nd cto
        procReqMasterDTO.setRecommendedBy02Id(approvalFlowEmployeeList.get(PRF_APPROVAL_02_NAME));
        // 3rd coo
        procReqMasterDTO.setRecommendedBy03Id(approvalFlowEmployeeList.get(PRF_APPROVAL_03_NAME));
        // 4th finance manager
        procReqMasterDTO.setRecommendedBy04Id(approvalFlowEmployeeList.get(PRF_APPROVAL_04_NAME));
        // 5th ceo
        procReqMasterDTO.setRecommendedBy05Id(approvalFlowEmployeeList.get(PRF_APPROVAL_05_NAME));
    }
}
