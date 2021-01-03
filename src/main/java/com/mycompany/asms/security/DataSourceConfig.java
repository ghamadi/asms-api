package com.mycompany.asms.security;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("app.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .username("root")
                .password("")
                .build();
    }
}