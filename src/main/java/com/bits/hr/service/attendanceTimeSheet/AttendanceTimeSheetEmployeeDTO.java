package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.Employee;

import java.util.List;

public class AttendanceTimeSheetEmployeeDTO {
    List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList;
    Employee employee;

    public List<AttendanceTimeSheetDTO> getAttendanceTimeSheetDTOList() {
        return attendanceTimeSheetDTOList;
    }

    public void setAttendanceTimeSheetDTOList(List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList) {
        this.attendanceTimeSheetDTOList = attendanceTimeSheetDTOList;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AttendanceTimeSheetEmployeeDTO() {
    }

    public AttendanceTimeSheetEmployeeDTO(List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList, Employee employee) {
        this.attendanceTimeSheetDTOList = attendanceTimeSheetDTOList;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "AttendanceTimeSheetEmployeeDTO{" +
            "attendanceTimeSheetDTOList=" + attendanceTimeSheetDTOList +
            ", employee=" + employee +
            '}';
    }
}
