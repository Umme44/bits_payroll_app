//package com.bits.hr.config;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import tech.jhipster.config.JHipsterConstants;
//import tech.jhipster.config.h2.H2ConfigurationHelper;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//
//@Configuration
//@EnableJpaRepositories({ "com.bits.hr.repository" })
//@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
//@EnableTransactionManagement
//public class DatabaseConfigurationOld {
//
//    private final Logger log = LoggerFactory.getLogger(DatabaseConfigurationOld.class);
//    private final Environment env;
//
//    public DatabaseConfigurationOld(Environment env) {
//        this.env = env;
//    }
//
//    /**
//     * Open the TCP port for the H2 database, so it is available remotely.
//     *
//     * @return the H2 database TCP server.
//     * @throws SQLException if the server failed to start.
//     */
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
//    public Object h2TCPServer() throws SQLException {
//        String port = getValidPortForH2();
//        log.debug("H2 database is available on port {}", port);
//        return H2ConfigurationHelper.createServer(port);
//    }
//
//    @Profile("!test")
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Profile("!test")
//    @Bean(name = Qualifiers.ATTENDANCE_SERVER_DB)
//    @ConfigurationProperties(prefix = "spring.second-datasource")
//    public DataSource secondaryDataSource() {
//
//        return DataSourceBuilder.create().build();
//    }
//
//    @Profile("!test")
//    @Bean(name = Qualifiers.ATTENDANCE_SERVER_JDBC_TEMPLATE)
//    public JdbcTemplate jdbcTemplate2(@Qualifier(Qualifiers.ATTENDANCE_SERVER_DB) DataSource secondDataSource) {
//        return new JdbcTemplate(secondDataSource);
//    }
//
//    private String getValidPortForH2() {
//        int port = Integer.parseInt(env.getProperty("server.port"));
//        if (port < 10000) {
//            port = 10000 + port;
//        } else {
//            if (port < 63536) {
//                port = port + 2000;
//            } else {
//                port = port - 2000;
//            }
//        }
//        return String.valueOf(port);
//    }
//
//    public static final class Qualifiers {
//        public static final String ATTENDANCE_SERVER_DB = "attendance-server-db";
//        public static final String ATTENDANCE_SERVER_JDBC_TEMPLATE = "jdbcTemplate2";
//    }
//}
