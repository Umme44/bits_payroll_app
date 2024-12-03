package com.bits.hr.service.LeaveManagement.leaveBalanceDetail;

import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveBalanceRepository;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.helperService.LeaveBalanceProcessService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceDetailViewService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveBalanceProcessService leaveBalanceProcessService;

    public List<LeaveBalanceDetailViewDTO> getYearlyProcessedLeaveBalance(int year, long employeeId) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        String pin = employeeRepository.getPinByEmployeeId(employeeId);
        String name = employeeRepository.getNameByEmployeeId(employeeId);

        List<LeaveBalance> leaveBalanceList = leaveBalanceRepository.findAllByYearAndEmployeeId(year, employeeId);

        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = new ArrayList<>();

        for (LeaveBalance leaveBalance : leaveBalanceList) {
            // process day based leave and service year type leave differently

            LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO = leaveBalanceProcessService.processLeaveBalance(
                leaveBalance,
                pin,
                name,
                startOfYear,
                endOfYear
            );
            leaveBalanceDetailViewDTOList.add(leaveBalanceDetailViewDTO);
        }
        return leaveBalanceDetailViewDTOList;
    }

    public int getRemainingBalance(int year, long employeeId, LeaveType leaveType) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        String pin = employeeRepository.getPinByEmployeeId(employeeId);
        String name = employeeRepository.getNameByEmployeeId(employeeId);

        try {
            Optional<LeaveBalance> leaveBalanceOptional = leaveBalanceRepository.findAllByYearAndEmployeeIdAndLeaveType(
                year,
                employeeId,
                leaveType
            );

            if (leaveBalanceOptional.isPresent()) {
                LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO = leaveBalanceProcessService.processLeaveBalance(
                    leaveBalanceOptional.get(),
                    pin,
                    name,
                    startOfYear,
                    endOfYear
                );

                return leaveBalanceDetailViewDTO.getDaysRemaining();
            }
        } catch (Exception ex) {
            return 0;
        }

        return 0;
    }
}
