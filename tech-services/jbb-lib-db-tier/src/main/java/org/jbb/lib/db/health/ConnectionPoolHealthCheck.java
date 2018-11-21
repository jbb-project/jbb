/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.health;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.health.JbbHealthCheck;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectionPoolHealthCheck extends JbbHealthCheck {

    private static final double THRESHOLD = 0.8;

    private final DataSource dataSource;

    private HikariDataSource hikariDataSource;

    @PostConstruct
    public void unwrapHikariDataSource() {
        try {
            hikariDataSource = dataSource.unwrap(HikariDataSource.class);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getName() {
        return "Database connection pool usage";
    }

    @Override
    protected Result check() throws Exception {
        HikariPoolMXBean poolMxBean = hikariDataSource.getHikariPoolMXBean();
        int activeConnections = poolMxBean.getActiveConnections();
        int totalConnections = poolMxBean.getTotalConnections();
        ResultBuilder resultBuilder = Result.builder()
            .withDetail("active", activeConnections)
            .withDetail("total", totalConnections);
        if (activeConnections / (double) totalConnections <= THRESHOLD) {
            resultBuilder = resultBuilder.healthy();
        } else {
            resultBuilder = resultBuilder.unhealthy()
                .withMessage("Connection pool usage is above 80%");
        }
        return resultBuilder.build();
    }
}
