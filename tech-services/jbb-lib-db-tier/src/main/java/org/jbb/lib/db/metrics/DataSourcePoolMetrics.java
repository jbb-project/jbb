/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.metrics;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import org.springframework.stereotype.Component;

import java.sql.SQLException;

import javax.sql.DataSource;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSourcePoolMetrics implements MeterBinder {

    private final DataSource dataSource;

    @Override
    public void bindTo(MeterRegistry registry) {
        try {
            if (dataSource.isWrapperFor(HikariDataSource.class)) {
                bindTo(dataSource.unwrap(HikariDataSource.class), registry);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void bindTo(HikariDataSource dataSource, MeterRegistry registry) {
        HikariPoolMXBean poolMxBean = dataSource.getHikariPoolMXBean();
        registry.gauge("jdbc.connections.active", poolMxBean.getActiveConnections());
        registry.gauge("jdbc.connections.idle", poolMxBean.getIdleConnections());
        registry.gauge("jdbc.connections.threadsAwaiting", poolMxBean.getThreadsAwaitingConnection());
        registry.gauge("jdbc.connections.total", poolMxBean.getTotalConnections());
    }
}
