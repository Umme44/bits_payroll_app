//package com.bits.hr.config.database;
//
//import java.sql.SQLException;
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import tech.jhipster.config.JHipsterConstants;
//import tech.jhipster.config.h2.H2ConfigurationHelper;
//
//@Configuration
//@EnableJpaRepositories({ "com.bits.hr.repository" })
//@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
//@EnableTransactionManagement
//public class DatabaseConfigurationLastStableOld {
//
//    private final Logger log = LoggerFactory.getLogger(DatabaseConfigurationLastStableOld.class);
//
//    private final Environment env;
//
//    public DatabaseConfigurationLastStableOld(Environment env) {
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
//    @Profile("!test")
//    @Bean(name = "entityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(EntityManagerFactoryBuilder builder) {
//        return builder.dataSource(defaultDataSource()).packages("com.bits.hr").persistenceUnit("default").build();
//    }
//
//    @Profile("!test")
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource")
//    public DataSource defaultDataSource() {
//        return defaultDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Profile("!test")
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSourceProperties defaultDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Profile("!test")
//    @Bean(name = "transactionManager")
//    @Primary
//    public JpaTransactionManager db2TransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }
//    //    @Bean
//    //    @Primary
//    //    public PhysicalNamingStrategy physicalNamingStrategy() {
//    //        return new CamelCaseToUnderscoresNamingStrategy();
//    //    }
//
//}
