package com.bits.hr.repository;

import com.bits.hr.config.database.CustomQualifiers;
import com.bits.hr.domain.IntegratedAttendance;
import com.bits.hr.service.mapper.AttendanceRowMapper;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Profile("enable-attendance-integration")
@Repository
public class AttendanceServer {

    private static final Logger log = LoggerFactory.getLogger(AttendanceServer.class);
    private final DataSource dataSource;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

    @Value("${spring.application.attendance-server.from}")
    private String startDate;

    @Value("${spring.application.attendance-server.terminals}")
    private String terminalList;

    private JdbcTemplate jdbcTemplate;

    public AttendanceServer(@Qualifier(CustomQualifiers.ATTENDANCE_SERVER_DB) DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<IntegratedAttendance> getData(Long last) {
        String sql = String.format(
            "SELECT id, emp_code, punch_time, terminal_id \n" +
            "FROM biotime.public.iclock_transaction \n" +
            "where punch_time>'%s' \n" +
            "  and emp_code is not null and emp_code !='' \n" +
            "  and id > %d " +
//            "  and ( terminal_id = 1 or terminal_id = 2 ) " +
            "order by id, emp_code limit %d",
            startDate,
            last,
            batchSize
        );

        log.debug(sql);

        List<IntegratedAttendance> attendanceList = jdbcTemplate.query(sql, new AttendanceRowMapper());
        return attendanceList;
    }
}
