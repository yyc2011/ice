package com.ice.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    private final String url;
    private final String user;
    private final String password;

    public FlywayConfig(
            @Value("${spring.flyway.url}") String url,
            @Value("${spring.flyway.user}") String user,
            @Value("${spring.flyway.password}") String password
    ) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
    }
}
