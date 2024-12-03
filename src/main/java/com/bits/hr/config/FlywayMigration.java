package com.bits.hr.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("no-liquibase")
public class FlywayMigration {

    public void migrate(DataSource dataSource) {
        Flyway
            .configure()
            .locations("classpath:config/migration")
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .baselineVersion("1")
            .table("Z_FLYWAY_MIGRATION_CHANGELOG")
            .sqlMigrationSeparator("__")
            .sqlMigrationPrefix("biTS_HR_")
            .sqlMigrationSuffixes(".sql")
            .load()
            .migrate();
    }
}
