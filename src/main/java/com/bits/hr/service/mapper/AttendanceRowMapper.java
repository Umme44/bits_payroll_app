package com.bits.hr.service.mapper;

import com.bits.hr.domain.IntegratedAttendance;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mapstruct.ReportingPolicy;
import org.springframework.jdbc.core.RowMapper;

public class AttendanceRowMapper implements RowMapper<IntegratedAttendance> {

    @Override
    public IntegratedAttendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        IntegratedAttendance attendance = new IntegratedAttendance();
        attendance.setId(rs.getLong("id"));
        attendance.setEmployeePin(rs.getString("emp_code"));
        attendance.setTimestamp(rs.getTimestamp("punch_time"));
        attendance.setTerminal(rs.getInt("terminal_id"));

        return attendance;
    }
}
