//package com.bits.hr.config;
//
//import java.sql.SQLException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import tech.jhipster.config.JHipsterConstants;
//import tech.jhipster.config.h2.H2ConfigurationHelper;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//public class PrimaryDatabaseConfig {
//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource")
//    public DataSourceProperties defaultDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource")
//    public DataSource defaultDataSource() {
//        return defaultDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "entityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
//        EntityManagerFactoryBuilder builder) {
//        return builder
//            .dataSource(defaultDataSource())
//            .packages(Input.class)
//            .persistenceUnit("default")
//            .build();
//    }
//
//    @Bean(name = "transactionManager")
//    @Primary
//    public JpaTransactionManager db2TransactionManager(@Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }
//}
