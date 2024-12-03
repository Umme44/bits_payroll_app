package com.bits.hr.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Profile("enable-attendance-integration")
@Configuration
@ConfigurationProperties("spring.second-datasource")
@RequiredArgsConstructor
public class AttendanceDbConfig extends HikariConfig {

    @Bean(CustomQualifiers.ATTENDANCE_SERVER_DB)
    public HikariDataSource dataSourceRead() {
        return new HikariDataSource(this);
    }

    @Bean(name = CustomQualifiers.ATTENDANCE_SERVER_JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate3(@Qualifier(CustomQualifiers.ATTENDANCE_SERVER_DB) DataSource reportDatasource) {
        return new JdbcTemplate(reportDatasource);
    }

    @Bean(name = CustomQualifiers.ATTENDANCE_SERVER_JDBC_TEMPLATE)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
        @Qualifier(CustomQualifiers.ATTENDANCE_SERVER_DB) DataSource reportDatasource
    ) {
        return new NamedParameterJdbcTemplate(reportDatasource);
    }
}
