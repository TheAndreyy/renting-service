package com.example.rentingservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void setDatabaseTransactionControl() {
        jdbcTemplate.execute("SET DATABASE TRANSACTION CONTROL MVLOCKS");
    }

}
