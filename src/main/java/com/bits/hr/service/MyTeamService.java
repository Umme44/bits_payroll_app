package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.MyTeamMemberAttendancesDTO;
import java.util.List;

public interface MyTeamService {
    List<MyTeamMemberAttendancesDTO> getMyTeamAttendance(Employee currentEmployee);

    boolean isMyTeamMember(Employee lineManager, long teamMemberId);
}
